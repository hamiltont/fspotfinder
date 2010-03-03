#ifndef LOTLAYOUT_HPP
#define LOTLAYOUT_HPP

#include <stdio.h>
#include <libxml/tree.h>
#include <string.h>
#include <vector>
#include <cv.h>

class LotLayout {

   public:
      LotLayout();
      LotLayout(char* xmlfilename);
      ~LotLayout();

      bool    parseXML(char* filename);
      bool    printRegions();

      bool    goToFirst();
      bool    goTo(int element_number);
      bool    goToNext();
      bool    goToPrevious();
      bool    isLast();
      int     getCurrentIndex();
      bool    parseCurrent(bool printValues = false);
      CvRect* getCurrentROI();
      bool    isPointInCurrentSpot(int x, int y, CvRect ROI);

   private:
      xmlDoc*        doc;
      xmlNode*       root_element;
      xmlNode*       cur_element;
      int            cur_element_number;
      std::vector<int>*   currentX;
      std::vector<int>*   currentY;
};

#endif
