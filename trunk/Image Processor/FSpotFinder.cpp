#define CV_NO_BACKWARD_COMPATIBILITY


#include <cv.h>
#include <highgui.h>
#include <math.h>
#include <LotLayout.hpp>

char wndname[] = "Hough";
char tbarname_threshold[] = "Threshold";
char tbarname_minline[] = "Minimum Line Length";
int edge_thresh = 7;
int hough_minline = 21;
bool pause_after_each = false;
LotLayout* lotLayout;
IplImage* src;
IplImage* dst;
IplImage* color_dst;
CvMemStorage* storage;
CvSeq* lines;

int update() {
   int lineScore = 0;

   cvCanny(src, dst, (float)edge_thresh*3, (float)edge_thresh*10, 3);
   cvCvtColor( dst, color_dst, CV_GRAY2BGR );

   lines = cvHoughLines2( dst, storage, CV_HOUGH_PROBABILISTIC, 1, CV_PI/180, hough_minline, hough_minline, 10 );

   CvRect* currentROI = lotLayout->getCurrentROI();

   for(int i = 0; i < lines->total; i++ )
   {
      CvPoint* line = (CvPoint*)cvGetSeqElem(lines,i);
      cvLine( color_dst, line[0], line[1], CV_RGB(255,0,0), 3, CV_AA, 0 );
      if(pause_after_each)
         fprintf(stdout, "\t\t\tTESTING X: %d Y: %d and X: %d Y: %d\n", line[0].x, line[0].y, line[1].x, line[1].y);
      if(lotLayout->isPointInCurrentSpot(line[0].x, line[0].y, *currentROI) || lotLayout->isPointInCurrentSpot(line[1].x, line[1].y, *currentROI))
         lineScore++;
   }

   cvShowImage( wndname, color_dst );
   cvShowImage( "Source", src );
   
   fprintf(stdout, "\tLines in region %d: %d\n", lotLayout->getCurrentIndex(), lineScore);
   
   return lineScore;
}

int main(int argc, char** argv)
{
   if(argc >= 3)
   {
      const char* filename = argv[1];
      char* xmlname = argv[2];
      src = cvLoadImage( filename, 0 );
      lotLayout = new LotLayout(xmlname);

      lotLayout->printRegions();
      if(argc >= 4) 
         pause_after_each = (argv[3][0] == 'y');
      else
         pause_after_each = false;
   } 
   else
   {
      fprintf(stdout, "Usage: ./FSpotFinder <jpeg file> <xml file> <optional pause after each spot y/n>\n\tExample:\n\t./FSpotFinder test.jpeg testlayout.xml y\n");      
      return -1;
   }    

   storage = cvCreateMemStorage(0);
   lines = 0;

   if( !src ) {
      fprintf(stdout, "Error loading image.\n");
      return -1;
   }

   dst = cvCreateImage( cvGetSize(src), 8, 1 );
   color_dst = cvCreateImage( cvGetSize(src), 8, 3 );

   cvNamedWindow( "Source", 1 );
   cvNamedWindow( wndname, 1 );

   fprintf(stdout, "Beginning linecounts...\n");
   lotLayout->goToFirst();

   while(true) {
      CvRect* tempRect = lotLayout->getCurrentROI();
      cvSetImageROI(src, *tempRect);
      cvSetImageROI(dst, *tempRect);
      cvSetImageROI(color_dst, *tempRect);
      update();
      delete tempRect;
      
      if(lotLayout->isLast())
         break;
         
      lotLayout->goToNext();
      
      if(pause_after_each)
         cvWaitKey(0);
   }

   cvWaitKey(0);

   return 0;
}

