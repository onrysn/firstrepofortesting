package com.mobit;

import java.util.List;

public interface IPrintingProcess {

	void print(List<PageData> pages)throws Exception;
	
	// Callbacks
	void begin_print(PageData page,PageFormatInfo pfi);
	void print_item(PrintItem item, INode node);
	void no_item(PrintItem item);
	void end_print(PageData page, PageFormatInfo pfi);
	
	
}
