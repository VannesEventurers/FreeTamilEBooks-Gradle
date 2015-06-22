package com.freetamilebooks.freetamilebooks.home;

public interface HomeItemListener {
	void DownloadPressed(BooksHomeParser.Books.Book singleItem);
	void OpenPressed(BooksHomeParser.Books.Book singleItem);
}
