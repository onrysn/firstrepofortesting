/*     */ package com.bixolon.printer;
/*     */ 
/*     */ import com.bixolon.common.BXLException;
/*     */ import com.bixolon.util.BXLUtility;
/*     */ import com.bixolon.util.JQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BXLPrinter2 extends BXLPrinter
/*     */ {
	
/*     */   //BXLPOSCommand posCommand;
/*     */   //BXLPrinterProperties properties;
/*     */   //int printerModel;
/*     */   
/*     */   public BXLPrinter2(int printerModel)
/*     */     throws BXLException
/*     */   {
				super(printerModel);
/*  19 */     this.printerModel = printerModel;
/*  20 */     properties = new BXLPrinterProperties();
/*  21 */     properties.initProperties(printerModel);
/*  22 */     posCommand = new BXLPOSCommand(properties);
/*  23 */     pagemodeBufferQ = new JQueue();
/*  24 */     normalBufferQ = new JQueue();
/*     */   }
/*     */   
/*     */   public int initPrinter()
/*     */   {
/*  29 */     normalBufferQ.clear();
/*  30 */     byte[] buffer = { 27, 64 };
/*  31 */     normalBufferQ.addLast(buffer);
/*  32 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printText(String data)
/*     */   {
/*  78 */     byte[] buffer = null;
/*     */     
/*  80 */     ValidateData validateData = new ValidateData(data);
/*  81 */     validateData.checkValidateParameter();
/*  82 */     validateData.execute(properties);
/*     */     
/*  84 */     buffer = BXLUtility.insertTail(buffer, validateData.getCommand());
/*     */     
/*  86 */     normalBufferQ.addLast(buffer);
/*  87 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int print1DBarcode(String data, int symbology, int height, int barWidth, int alignment, int textPosition)
/*     */   {
/* 115 */     byte[] buffer = null;
/*     */     try
/*     */     {
/* 118 */       CommandProcess commsCmd = new PrintBarcode(data, symbology, height, barWidth, alignment, textPosition);
/* 119 */       commsCmd.checkValidateParameter();
/* 120 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties));
/*     */       
/* 122 */       normalBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 126 */       e.printStackTrace();
/* 127 */       return 106;
/*     */     }
/* 129 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printPDF417(String data, int columnsNumber, int rowsNumber, int moduleWidth, int moduleHeight, int errorCorrectionLevel, int optionSelect, int alignment)
/*     */   {
/* 148 */     byte[] buffer = null;
/*     */     try
/*     */     {
/* 151 */       CommandProcess commsCmd = new PrintBarcode(data, columnsNumber, rowsNumber, moduleWidth, moduleHeight, errorCorrectionLevel, optionSelect, 
/* 152 */         alignment);
/* 153 */       commsCmd.checkValidateParameter();
/* 154 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties));
/*     */       
/* 156 */       normalBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 160 */       e.printStackTrace();
/* 161 */       return 106;
/*     */     }
/* 163 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printQRCode(String data, int modelSelect, int moduleSize, int errorCorrectionLevel, int alignment)
/*     */   {
/* 178 */     byte[] buffer = null;
/*     */     try
/*     */     {
/* 181 */       CommandProcess commsCmd = new PrintBarcode(data, modelSelect, moduleSize, errorCorrectionLevel, alignment);
/* 182 */       commsCmd.checkValidateParameter();
/* 183 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties));
/*     */       
/* 185 */       normalBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 189 */       e.printStackTrace();
/* 190 */       return 106;
/*     */     }
/* 192 */     return 0;
/*     */   }
/*     */   
/*     */   public int printBitmap(String filename, int width, int alignment)
/*     */   {
/* 197 */     byte[] buffer = null;
/*     */     try
/*     */     {
/* 200 */       CommandProcess commsCmd = new PrintBitmap(filename, width, alignment, properties.getRecLineWidth());
/* 201 */       commsCmd.checkValidateParameter();
/* 202 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties, false));
/*     */       
/* 204 */       normalBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 208 */       e.printStackTrace();
/* 209 */       return 106;
/*     */     }
/* 211 */     return 0;
/*     */   }
/*     */   
/*     */   public int cutPaper()
/*     */   {
/* 216 */     byte[] buffer = null;
/* 217 */     buffer = CommandSet.getCutPaperCommand(100);
/* 218 */     normalBufferQ.addLast(buffer);
/* 219 */     return 0;
/*     */   }
/*     */   
/* 222 */   boolean useBlackMark = false;
/*     */   JQueue pagemodeBufferQ;
/*     */   JQueue normalBufferQ;
/*     */   
/*     */   public int beginPageMode(int startPosX, int startPosY, int endPosX, int endPosY, boolean initPrinter) {
/* 227 */     return beginPageMode(startPosX, startPosY, endPosX, endPosY, initPrinter, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int beginPageMode(int startPosX, int startPosY, int endPosX, int endPosY, boolean initPrinter, boolean useBlackMark)
/*     */   {
/* 244 */     if (properties.getPagemodeControl() == 1)
/* 245 */       return 106;
/* 246 */     properties.setPagemodeControl(1);
/* 247 */     properties.setPageModePrintArea(startPosX + "," + startPosY + "," + endPosX + "," + endPosY);
/* 248 */     byte[] buffer = null;
/* 249 */     if (initPrinter)
/* 250 */       buffer = new byte[] { 27, 64 };
/* 251 */     this.useBlackMark = useBlackMark;
/* 252 */     if (this.useBlackMark)
/*     */     {
/* 254 */       buffer = BXLUtility.insertTail(buffer, new byte[] { 8, 76, 76 });
/*     */     }
/*     */     
/*     */ 
/* 258 */     buffer = BXLUtility.insertTail(buffer, new byte[] { 27, 76 });
/* 259 */     buffer = BXLUtility.insertTail(buffer, properties.getPageModePrintAreaCommand());
/* 260 */     pagemodeBufferQ.addLast(buffer);
/* 261 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printTextInPageMode(int direction, int posX, int posY, String data)
/*     */   {
/* 287 */     if (properties.getPagemodeControl() != 1)
/* 288 */       return 106;
/* 289 */     byte[] buffer = null;
/*     */     
/* 291 */     if (properties.getPageModePrintDirection() != direction)
/* 292 */       properties.setPageModePrintDirection(direction);
/* 293 */     byte[] temp = null;
/* 294 */     temp = properties.getPageModePrintDirectionCommand();
/* 295 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/* 297 */     properties.setPageModeHorizontalPosition(posX);
/* 298 */     properties.setPageModeVerticalPosition(posY);
/* 299 */     temp = properties.getPagemodeHorizontalPositionCommand();
/* 300 */     buffer = BXLUtility.insertTail(buffer, temp);
/* 301 */     temp = properties.getPageModeVerticalPositionCommand();
/* 302 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/* 304 */     ValidateData validateData = new ValidateData(data);
/* 305 */     validateData.checkValidateParameter();
/* 306 */     validateData.execute(properties);
/*     */     
/* 308 */     buffer = BXLUtility.insertTail(buffer, validateData.getCommand());
/*     */     
/* 310 */     pagemodeBufferQ.addLast(buffer);
/* 311 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int print1DBarcodeInPageMode(int direction, int posX, int posY, String data, int symbology, int height, int barWidth, int alignment, int textPosition)
/*     */   {
/* 354 */     if (properties.getPagemodeControl() != 1)
/* 355 */       return 106;
/* 356 */     byte[] buffer = null;
/*     */     
/* 358 */     if (properties.getPageModePrintDirection() != direction)
/* 359 */       properties.setPageModePrintDirection(direction);
/* 360 */     byte[] temp = null;
/* 361 */     temp = properties.getPageModePrintDirectionCommand();
/* 362 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/* 364 */     properties.setPageModeHorizontalPosition(posX);
/* 365 */     properties.setPageModeVerticalPosition(posY);
/* 366 */     temp = properties.getPagemodeHorizontalPositionCommand();
/* 367 */     buffer = BXLUtility.insertTail(buffer, temp);
/* 368 */     temp = properties.getPageModeVerticalPositionCommand();
/* 369 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/*     */     try
/*     */     {
/* 373 */       CommandProcess commsCmd = new PrintBarcode(data, symbology, height, barWidth, alignment, textPosition);
/* 374 */       commsCmd.checkValidateParameter();
/* 375 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties));
/*     */       
/* 377 */       pagemodeBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 381 */       e.printStackTrace();
/* 382 */       return 106;
/*     */     }
/* 384 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printPDF417InPageMode(int direction, int posX, int posY, String data, int columnsNumber, int rowsNumber, int moduleWidth, int moduleHeight, int errorCorrectionLevel, int optionSelect, int alignment)
/*     */   {
/* 403 */     if (properties.getPagemodeControl() != 1)
/* 404 */       return 106;
/* 405 */     byte[] buffer = null;
/*     */     
/* 407 */     if (properties.getPageModePrintDirection() != direction)
/* 408 */       properties.setPageModePrintDirection(direction);
/* 409 */     byte[] temp = null;
/* 410 */     temp = properties.getPageModePrintDirectionCommand();
/* 411 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/* 413 */     properties.setPageModeHorizontalPosition(posX);
/* 414 */     properties.setPageModeVerticalPosition(posY);
/* 415 */     temp = properties.getPagemodeHorizontalPositionCommand();
/* 416 */     buffer = BXLUtility.insertTail(buffer, temp);
/* 417 */     temp = properties.getPageModeVerticalPositionCommand();
/* 418 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     try
/*     */     {
/* 421 */       CommandProcess commsCmd = new PrintBarcode(data, columnsNumber, rowsNumber, moduleWidth, moduleHeight, errorCorrectionLevel, optionSelect, 
/* 422 */         alignment);
/* 423 */       commsCmd.checkValidateParameter();
/* 424 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties));
/*     */       
/* 426 */       pagemodeBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 430 */       e.printStackTrace();
/* 431 */       return 106;
/*     */     }
/* 433 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printQRCodeInPageMode(int direction, int posX, int posY, String data, int modelSelect, int moduleSize, int errorCorrectionLevel, int alignment)
/*     */   {
/* 448 */     if (properties.getPagemodeControl() != 1)
/* 449 */       return 106;
/* 450 */     byte[] buffer = null;
/*     */     
/* 452 */     if (properties.getPageModePrintDirection() != direction)
/* 453 */       properties.setPageModePrintDirection(direction);
/* 454 */     byte[] temp = null;
/* 455 */     temp = properties.getPageModePrintDirectionCommand();
/* 456 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/* 458 */     properties.setPageModeHorizontalPosition(posX);
/* 459 */     properties.setPageModeVerticalPosition(posY);
/* 460 */     temp = properties.getPagemodeHorizontalPositionCommand();
/* 461 */     buffer = BXLUtility.insertTail(buffer, temp);
/* 462 */     temp = properties.getPageModeVerticalPositionCommand();
/* 463 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     try
/*     */     {
/* 466 */       CommandProcess commsCmd = new PrintBarcode(data, modelSelect, moduleSize, errorCorrectionLevel, alignment);
/* 467 */       commsCmd.checkValidateParameter();
/* 468 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties));
/*     */       
/* 470 */       pagemodeBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 474 */       e.printStackTrace();
/* 475 */       return 106;
/*     */     }
/* 477 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int printBitmapInPageMode(int direction, int posX, int posY, String filename, int width, int alignment)
/*     */   {
/* 517 */     if (properties.getPagemodeControl() != 1)
/* 518 */       return 106;
/* 519 */     byte[] buffer = null;
/*     */     
/* 521 */     if (properties.getPageModePrintDirection() != direction)
/* 522 */       properties.setPageModePrintDirection(direction);
/* 523 */     byte[] temp = null;
/* 524 */     temp = properties.getPageModePrintDirectionCommand();
/* 525 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/* 527 */     properties.setPageModeHorizontalPosition(posX);
/* 528 */     properties.setPageModeVerticalPosition(posY);
/* 529 */     temp = properties.getPagemodeHorizontalPositionCommand();
/* 530 */     buffer = BXLUtility.insertTail(buffer, temp);
/* 531 */     temp = properties.getPageModeVerticalPositionCommand();
/* 532 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/*     */     try
/*     */     {
/* 536 */       CommandProcess commsCmd = new PrintBitmap(filename, width, alignment, properties.getRecLineWidth());
/* 537 */       commsCmd.checkValidateParameter();
/* 538 */       buffer = BXLUtility.insertTail(buffer, commsCmd.getCommand(properties, false));
/*     */       
/* 540 */       pagemodeBufferQ.addLast(buffer);
/*     */     }
/*     */     catch (BXLException e)
/*     */     {
/* 544 */       e.printStackTrace();
/* 545 */       return 106;
/*     */     }
/* 547 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int endPageMode(boolean addFeed)
/*     */   {
/* 561 */     if (properties.getPagemodeControl() != 1)
/* 562 */       return 106;
/* 563 */     byte[] buffer = { 12 };
/* 564 */     pagemodeBufferQ.addLast(buffer);
/* 565 */     if (!useBlackMark)
/*     */     {
/* 567 */       if (addFeed) {
/* 568 */         pagemodeBufferQ.addLast(new byte[] { 10, 10 });
/*     */       }
/*     */     }
/*     */     else {
/* 572 */       pagemodeBufferQ.addLast(new byte[] { 12 });
/* 573 */       pagemodeBufferQ.addLast(new byte[] { 8, 76, 82 });
/* 574 */       useBlackMark = false;
/*     */     }
/* 576 */     properties.setPagemodeControl(3);
/* 577 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int endPageMode(boolean addFeed, boolean keepLabelMode)
/*     */   {
/* 592 */     if (properties.getPagemodeControl() != 1)
/* 593 */       return 106;
/* 594 */     byte[] buffer = { 12 };
/* 595 */     pagemodeBufferQ.addLast(buffer);
/* 596 */     if (!useBlackMark)
/*     */     {
/* 598 */       if (addFeed) {
/* 599 */         pagemodeBufferQ.addLast(new byte[] { 10, 10 });
/*     */       }
/*     */     }
/*     */     else {
/* 603 */       pagemodeBufferQ.addLast(new byte[] { 12 });
/* 604 */       if (!keepLabelMode)
/*     */       {
/* 606 */         pagemodeBufferQ.addLast(new byte[] { 8, 76, 82 });
/* 607 */         useBlackMark = false;
/*     */       }
/*     */     }
/*     */     
/* 611 */     properties.setPagemodeControl(3);
/* 612 */     return 0;
/*     */   }
/*     */   
/*     */   public int changeReceiptMode()
/*     */   {
/* 617 */     useBlackMark = true;
/* 618 */     normalBufferQ.addLast(new byte[] { 8, 76, 82 });
/* 619 */     return 0;
/*     */   }
/*     */   
/*     */   public int changeLabelMode()
/*     */   {
/* 624 */     useBlackMark = false;
/* 625 */     normalBufferQ.addLast(new byte[] { 8, 76, 76 });
/* 626 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getPagemodeBuffer()
/*     */   {
/* 649 */     byte[] buffer = null;
/* 650 */     while (!pagemodeBufferQ.isEmpty())
/*     */     {
/* 652 */       byte[] temp = (byte[])pagemodeBufferQ.pop();
/* 653 */       buffer = BXLUtility.insertTail(buffer, temp);
/*     */     }
/* 655 */     return buffer;
/*     */   }
/*     */   
/*     */   public byte[] getNormalBuffer()
/*     */   {
/* 660 */     byte[] buffer = null;
/* 661 */     while (!normalBufferQ.isEmpty())
/*     */     {
/* 663 */       byte[] temp = (byte[])normalBufferQ.pop();
/* 664 */       buffer = BXLUtility.insertTail(buffer, temp);
/*     */     }
/* 666 */     return buffer;
/*     */   }
/*     */   
/*     */   public synchronized String getCharacterSetList()
/*     */   {
/* 707 */     return properties.getCharacterSetList();
/*     */   }
/*     */   
/*     */   public synchronized String getFontTypefaceList()
/*     */   {
/* 712 */     return properties.getFontTypefaceList();
/*     */   }
/*     */   
/*     */   public synchronized String getPageModeArea()
/*     */   {
/* 717 */     return properties.getPageModeArea();
/*     */   }
/*     */   
/*     */   public synchronized int getCharacterSet()
/*     */   {
/* 722 */     return properties.getCharacterSet();
/*     */   }
/*     */   
/*     */   public synchronized void setCharacterSet(int characterSet)
/*     */   {
/* 727 */     if (characterSet != properties.getCharacterSet())
/*     */     {
/* 729 */       properties.setCharacterSet(characterSet);
/*     */       
/* 731 */       if (properties.getPagemodeControl() == 1)
/*     */       {
/* 733 */         pagemodeBufferQ.addLast(CommandSet.getCharacterSetCommand(properties.getCharacterSet()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int setRecFont(int recFont)
/*     */   {
/* 740 */     if (recFont != properties.getRecFont())
/*     */     {
/* 742 */       properties.setRecFont(recFont);
/* 743 */       byte[] command = CommandSet.getFontTypeCommand(recFont);
/*     */       
/* 745 */       if (properties.getPagemodeControl() == 1) {
/* 746 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 748 */         normalBufferQ.addLast(command);
/*     */     }
/* 750 */     return 0;
/*     */   }
/*     */   
/*     */   public int setInternationalCharSet(int internationalCharSet)
/*     */   {
/* 755 */     if (properties.getInternationalCharSet() != internationalCharSet)
/*     */     {
/* 757 */       properties.setInternationalCharSet(internationalCharSet);
/* 758 */       byte[] command = CommandSet.getIntlCharsetCommand(internationalCharSet);
/* 759 */       if (properties.getPagemodeControl() == 1) {
/* 760 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 762 */         normalBufferQ.addLast(command);
/*     */     }
/* 764 */     return 0;
/*     */   }
/*     */   
/*     */   public int setBold(boolean bold)
/*     */   {
/* 769 */     if (properties.getRecBold() != bold)
/*     */     {
/* 771 */       properties.setRecBold(bold);
/* 772 */       int boldValue = bold ? 1 : 0;
/* 773 */       byte[] command = CommandSet.getBoldCommand(boldValue);
/* 774 */       if (properties.getPagemodeControl() == 1) {
/* 775 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 777 */         normalBufferQ.addLast(command);
/*     */     }
/* 779 */     return 0;
/*     */   }
/*     */   
/*     */   public int setUnderline(boolean underline)
/*     */   {
/* 784 */     if (properties.getRecUnderline() != underline)
/*     */     {
/* 786 */       properties.setRecUnderline(underline);
/* 787 */       int underlineValue = underline ? 1 : 0;
/* 788 */       byte[] command = CommandSet.getUnderlineCommand(underlineValue);
/* 789 */       if (properties.getPagemodeControl() == 1) {
/* 790 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 792 */         normalBufferQ.addLast(command);
/*     */     }
/* 794 */     return 0;
/*     */   }
/*     */   
/*     */   public int setBWReverse(boolean reverse)
/*     */   {
/* 799 */     if (properties.getRecBWReverse() != reverse)
/*     */     {
/* 801 */       properties.setRecBWReverse(reverse);
/* 802 */       int reverseValue = reverse ? 1 : 0;
/* 803 */       byte[] command = CommandSet.getReverseWhiteBlackCommand(reverseValue);
/* 804 */       if (properties.getPagemodeControl() == 1) {
/* 805 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 807 */         normalBufferQ.addLast(command);
/*     */     }
/* 809 */     return 0;
/*     */   }
/*     */   
/*     */   public int setAlignment(int alignment)
/*     */   {
/* 814 */     if (properties.getAlignment() != alignment)
/*     */     {
/* 816 */       properties.setAlignment(alignment);
/* 817 */       byte[] command = CommandSet.getAlignmentCommand(alignment);
/* 818 */       if (properties.getPagemodeControl() == 1) {
/* 819 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 821 */         normalBufferQ.addLast(command);
/*     */     }
/* 823 */     return 0;
/*     */   }
/*     */   
/*     */   public int setFontScale(int wideScale, int heightScale)
/*     */   {
/* 828 */     if ((properties.getWideScale() != wideScale) || (properties.getHeightScale() != heightScale))
/*     */     {
/* 830 */       properties.setWideScale(wideScale);
/* 831 */       properties.setHeightScale(heightScale);
/* 832 */       byte[] command = CommandSet.getCharacterScaleCommand(wideScale, heightScale);
/* 833 */       if (properties.getPagemodeControl() == 1) {
/* 834 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 836 */         normalBufferQ.addLast(command);
/*     */     }
/* 838 */     return 0;
/*     */   }
/*     */   
/*     */   public synchronized String getRecLineCharsList()
/*     */   {
/* 843 */     return properties.getRecLineCharsList();
/*     */   }
/*     */   
/*     */   public synchronized int getRecLineHeight()
/*     */   {
/* 848 */     return properties.getRecLineHeight();
/*     */   }
/*     */   
/*     */   public synchronized int getRecLineSpacing()
/*     */   {
/* 853 */     return properties.getRecLineSpacing();
/*     */   }
/*     */   
/*     */   public int setRecLineSpacing(int recLineSpacing)
/*     */   {
/* 858 */     if (properties.getRecLineSpacing() != recLineSpacing)
/*     */     {
/* 860 */       properties.setRecLineSpacing(recLineSpacing);
/* 861 */       byte[] command = CommandSet.getLineSpacingCommand(recLineSpacing);
/* 862 */       if (properties.getPagemodeControl() == 1) {
/* 863 */         pagemodeBufferQ.addLast(command);
/*     */       } else
/* 865 */         normalBufferQ.addLast(command);
/*     */     }
/* 867 */     return 0;
/*     */   }
/*     */   
/*     */   public synchronized int getRecLineWidth()
/*     */   {
/* 872 */     return properties.getRecLineWidth();
/*     */   }
/*     */ 

	public int printNVImageInPageMode(int posX, int posY, byte nvNumber)
/*     */   {
/* 517 */     if (properties.getPagemodeControl() != 1)
/* 518 */       return 106;
/* 519 */     byte[] buffer = null;
/*     */     byte[] temp = null;
/* 521 */     
/*     */     
/* 527 */     properties.setPageModeHorizontalPosition(posX);
/* 528 */     properties.setPageModeVerticalPosition(posY);
/* 529 */     temp = properties.getPagemodeHorizontalPositionCommand();
/* 530 */     buffer = BXLUtility.insertTail(buffer, temp);
/* 531 */     temp = properties.getPageModeVerticalPositionCommand();
/* 532 */     buffer = BXLUtility.insertTail(buffer, temp);
/*     */     
/*     */     
				byte [] cmd = {28, 112, nvNumber, 0}; // GS p
/* 536 */       
/* 538 */       buffer = BXLUtility.insertTail(buffer, cmd);
/*     */       
/* 540 */       pagemodeBufferQ.addLast(buffer);
/*     */     
/* 547 */     return 0;
/*     */   }


}

/* Location:           Z:\PROJECTS\IsemriAndroid\bixolon\libs\BXLPrinter.jar
 * Qualified Name:     com.bixolon.printer.BXLPrinter
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.7.1
 */
