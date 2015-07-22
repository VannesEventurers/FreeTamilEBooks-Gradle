package com.jskaleel.fte.home;

public interface HomeItemListener {
	void DownloadPressed(BooksHomeParser.Books.Book singleItem);
	void OpenPressed(BooksHomeParser.Books.Book singleItem);
}
