package de.shiru.decryptor.core;

import cz.vutbr.web.css.CSSException;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.RuleSet;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Theme {
    private String name;
    private String author;
    private String comment;
    private URL url;
    private String path;

    public Theme(String themePath) throws IOException, ThemeUnvalidException, CSSException {
        try(var stream = new FileInputStream(themePath)) {
            loadProperties(stream.readAllBytes());
        }
        path = themePath;
        var f = new File(path);
        try {
            url = f.toURI().toURL();
        } catch (MalformedURLException ignored) {}
    }

    public Theme(byte[] themeContent, URL url) throws ThemeUnvalidException, CSSException, IOException {
        loadProperties(themeContent);
        this.url = url;
    }

    private void loadProperties(byte[] themeContent) throws ThemeUnvalidException, CSSException, IOException {
        var styleSheet = CSSFactory.parseString(new String(themeContent), null);
        try {
            RuleSet rule = (RuleSet) styleSheet.get(0);
            if(!rule.getSelectors()[0].toString().equals(".theme")) throw new ThemeUnvalidException("Please start with the theme rule in stylesheet !");

            var nameDeclaration = (Declaration) rule.get(0);
            if(!nameDeclaration.getProperty().equals("-name")) throw new ThemeUnvalidException("name property expected in stylesheet !");
            name = (String) nameDeclaration.get(0).getValue();

            var authorDeclaration = (Declaration) rule.get(1);
            if(!authorDeclaration.getProperty().equals("-author")) throw new ThemeUnvalidException("author property expected in stylesheet !");
            author = (String) authorDeclaration.get(0).getValue();

            var commentDeclaration = (Declaration) rule.get(2);
            if(!commentDeclaration.getProperty().equals("-comment")) throw new ThemeUnvalidException("comment property expected in stylesheet !");
            comment = (String) commentDeclaration.get(0).getValue();
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