#include "LotLayout.hpp"

LotLayout::LotLayout() : doc(NULL), root_element(NULL), 
   cur_element(NULL), cur_element_number(0), currentX(NULL), currentY(NULL)
{

}

LotLayout::LotLayout(char* xmlfilename) : doc(NULL), root_element(NULL), 
   cur_element(NULL), cur_element_number(0),  currentX(NULL), currentY(NULL)
{
   parseXML(xmlfilename);
}

LotLayout::~LotLayout()
{
   xmlFreeDoc(doc);
   xmlCleanupParser();
}

bool LotLayout::parseXML(char* filename)
{
   xmlFree(doc);
   doc = xmlReadFile(filename, NULL, 0);
   if (doc == NULL) {
      fprintf(stderr, "Failed to parse %s\n", filename);
      return false;
   } else {
      root_element = xmlDocGetRootElement(doc);
      xmlChar * nodename = xmlCharStrdup("regions");
      while(xmlStrncmp(root_element->name, nodename, 7)) {
         if(root_element->children) {
            root_element = root_element->children;
         } else {
            fprintf(stderr, "Failed to parse %s\n\tRoot node not properly named.", filename);
            xmlFree(nodename);
            return false;
         }
      }
      if(root_element->children && root_element->children->next) {
         cur_element = root_element->children->next;
         cur_element_number = 0;
      } else {
         fprintf(stderr, "Failed to parse %s\n\tNo regions defined.", filename);
         xmlFree(nodename);         
         return false;
      }
      xmlFree(nodename);      
      return true;
   }
}

bool LotLayout::printRegions() {
   fprintf(stdout, "Printing regions...\n");
   goToFirst();
   while(true) {
      fprintf(stdout, "Current Index: %d\n", getCurrentIndex());
      parseCurrent(true);
      
      CvRect* tempRect = getCurrentROI();
      fprintf(stdout, "\tROI:\n");
      fprintf(stdout, "\t\tX:%d\n", tempRect->x);
      fprintf(stdout, "\t\tY:%d\n", tempRect->y);
      fprintf(stdout, "\t\twidth:%d\n", tempRect->width);
      fprintf(stdout, "\t\theight:%d\n", tempRect->height);
      
      delete tempRect;
      
      if(isLast()) {
         fprintf(stdout, "Done.\n");
         break;
      }
      if(cur_element == NULL) {
         fprintf(stderr, "Failure.  Current is NULL.\n");
      }
      goToNext();
   }
   return true;
}

bool LotLayout::goToNext() {
   if(cur_element && cur_element->next && cur_element->next->next) {
      cur_element = cur_element->next->next;
      cur_element_number++;
      return true;
   } else {
      return false;
   }
}

bool LotLayout::goToPrevious() {
   if(cur_element && cur_element->prev && cur_element->prev->prev) {
      cur_element = cur_element->prev->prev;
      cur_element_number--;
      return true;
   } else {
      return false;
   }
}

bool LotLayout::goToFirst() {
   if(root_element && root_element->children && root_element->children->next) {
      cur_element          = root_element->children->next;
      cur_element_number   = 0;
      return true;
   } else {
      fprintf(stderr, "No file loaded or file contains no regions.\n");
      return false;
   }
}

bool LotLayout::isLast() {
   return root_element->last == cur_element->next;
}

int LotLayout::getCurrentIndex() {
   return cur_element_number;
}

bool LotLayout::goTo(int element_number) {
   if(cur_element) {
      xmlNode  *old_element = cur_element;
      int      old_element_number = cur_element_number;
      if(cur_element_number == element_number) {
         return true;
      } else if (element_number > cur_element_number) {
         while((element_number - cur_element_number) && cur_element->next && cur_element->next->next) {
            cur_element = cur_element->next->next;
            cur_element_number++;
         }
         if(element_number - cur_element_number) {
            fprintf(stderr, "Index out of range.\n");
            cur_element = old_element;
            cur_element_number = old_element_number;
            return false;
         } else {
            return true;
         }
      } else {
         while((element_number - cur_element_number) && cur_element->prev && cur_element->prev->prev) {
            cur_element = cur_element->prev->prev;
            cur_element_number--;
         }
         if(element_number - cur_element_number) {
            fprintf(stderr, "Index out of range.\n");
            cur_element = old_element;
            cur_element_number = old_element_number;
            return false;
         } else {
            return true;
         }
      }
   } else if(root_element && root_element->children && root_element->children->next) {
      cur_element          = root_element->children->next;
      cur_element_number   = 0;
      return goTo(element_number);
   } else {
      fprintf(stderr, "No file loaded or file contains no regions.\n");
      return false;
   }
}

CvRect * LotLayout::getCurrentROI() {
   if(parseCurrent()) {
      CvRect* rtnValue = new CvRect();
      int max_x = (*currentX)[0];
      int min_x = (*currentX)[0];
      int max_y = (*currentY)[0];
      int min_y = (*currentY)[0];

      for(int i = 1; i < currentX->size(); i++) {
         if((*currentX)[i] > max_x)
            max_x = (*currentX)[i];
         if((*currentX)[i] < min_x)
            min_x = (*currentX)[i];   
         if((*currentY)[i] > max_y)
            max_y = (*currentY)[i];
         if((*currentY)[i] < min_y)
            min_y = (*currentY)[i];  
      }
      
      rtnValue->x = min_x;
      rtnValue->width = max_x - min_x;
      rtnValue->y = min_y;
      rtnValue->height = max_y - min_y;
      
      return rtnValue;
   } else {
      return NULL;
   }
}

bool LotLayout::parseCurrent(bool printValues) {
   if(currentX != NULL)
      delete currentX;
   if(currentY != NULL)
      delete currentY;
   
   currentX = new std::vector<int>();
   currentY = new std::vector<int>();
   
   xmlChar* xString =  xmlCharStrdup("x");
   xmlChar* yString =  xmlCharStrdup("y");
   
   if(cur_element && cur_element->children) {
      xmlNode *cur_point = cur_element->children->next;
      while(cur_point != NULL) {
         xmlChar* XtempString = xmlGetProp(cur_point, xString);
         xmlChar* YtempString = xmlGetProp(cur_point, yString);
         
         currentX->push_back(atoi((char *)XtempString));
         currentY->push_back(atoi((char *)YtempString));

         if(printValues)
            fprintf(stdout, "\tX: %s, Y: %s \n", XtempString, YtempString);
         
         xmlFree(XtempString);
         xmlFree(YtempString);
         
         if(cur_point == cur_element->last)
            break;
            
         cur_point = cur_point->next->next;
      }
   } else {
      fprintf(stderr, "Error parsing current node.\n");
      xmlFree(xString);
      xmlFree(yString);
      return false;
   }
   return true;
}

bool LotLayout::isPointInCurrentSpot(int x, int y, CvRect ROI) {
   bool rtnvalue = false;
   x += ROI.x;
   y += ROI.y;
   int j = currentX->size() - 1;
   for (int i = 0; i < currentX->size(); j = i++) {
      if ( (((*currentY)[i]>y) != ((*currentY)[j]>y)) &&
            (x < ((*currentX)[j]-(*currentX)[i]) * (y-(*currentY)[i]) / ((*currentY)[j]-(*currentY)[i]) + (*currentX)[i]) ) {
         rtnvalue = !rtnvalue;
      }
   }
   return rtnvalue;
}
