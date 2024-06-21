module graph {
	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;
	exports yuzhou.gits.graph.javafx;
	opens yuzhou.gits.graph.javafx to javafx.base;
}