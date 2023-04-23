package de.shiru.decryptor.core;

import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Theme {
    private String name;
    private String author;
    private String comment;
    private URL url;
    private String path;

    public Theme(String themePath) throws IOException, ThemeUnvalidException {
        try(var stream = new FileInputStream(themePath)) {
            loadProperties(stream.readAllBytes());
        }
        path = themePath;
        var f = new File(path);
        try {
            url = f.toURI().toURL();
        } catch (MalformedURLException ignored) {}
    }

    public Theme(byte[] themeContent, URL url) throws ThemeUnvalidException {
        loadProperties(themeContent);
        this.url = url;
    }

    private void loadProperties(byte[] themeContent) throws ThemeUnvalidException {
        var reader = CSSReader.readFromStringStream(new String(themeContent), new CSSReaderSettings());
        try {
            var ruleName = reader.getStyleRuleAtIndex(0).getSelectorAtIndex(0).getMemberAtIndex(0).getAsCSSString();
            if(!ruleName.equals(".theme")) throw new ThemeUnvalidException("Please start with the theme rule in stylesheet !");

            var nameDeclaration = reader.getStyleRuleAtIndex(0).getDeclarationAtIndex(0);
            if(!nameDeclaration.getProperty().equals("-name")) throw new ThemeUnvalidException("name property expected in stylesheet !");
            var themeName = nameDeclaration.getExpression().getMemberAtIndex(0).getAsCSSString();

            var authorDeclaration = reader.getStyleRuleAtIndex(0).getDeclarationAtIndex(1);
            if(!authorDeclaration.getProperty().equals("-author")) throw new ThemeUnvalidException("author property expected in stylesheet !");
            var authorName = authorDeclaration.getExpression().getMemberAtIndex(0).getAsCSSString();

            var commentDeclaration = reader.getStyleRuleAtIndex(0).getDeclarationAtIndex(2);
            if(!commentDeclaration.getProperty().equals("-comment")) throw new ThemeUnvalidException("comment property expected in stylesheet !");
            var commentName = commentDeclaration.getExpression().getMemberAtIndex(0).getAsCSSString();

            name = themeName.substring(1, themeName.length() - 1);
            author = authorName.substring(1, authorName.length() - 1);
            comment = commentName.substring(1, commentName.length() - 1);
        } catch (Exception e) {
            if(e instanceof ThemeUnvalidException) throw e;
            throw new ThemeUnvalidException("Error while reading css theme !");
        }
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public String getPath() {
        return path;
    }

    public URL getURL() {
        return url;
    }

    public static class ThemeUnvalidException extends Exception {
        private final String message;

        public ThemeUnvalidException(String msg) {
            message = msg;
        }

        @Override
        public String getMessage() {
            return message;
        }

    }

}