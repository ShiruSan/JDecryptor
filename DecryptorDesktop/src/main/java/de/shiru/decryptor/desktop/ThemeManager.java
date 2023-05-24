package de.shiru.decryptor.desktop;

import de.shiru.decryptor.core.Theme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ThemeManager {
    private final List<Theme> themeList;
    private Consumer<Theme> onThemeChange;
    private Theme currentTheme;

    public ThemeManager() throws Exception {
        themeList = new ArrayList<>();
        var dir = System.getProperty("user.dir");
        var dirFile = new File(dir, "Themes");
        var resource = getClass().getResource("/fxml/Decryptor.css");
        try(var stream = getClass().getResourceAsStream("/fxml/Decryptor.css")) {
            themeList.add(new Theme(stream.readAllBytes(), resource));
        }
        if(!(dirFile.exists() && dirFile.isDirectory())) return;
        for(var file : Objects.requireNonNull(dirFile.listFiles())) {
            try {
                themeList.add(new Theme(file.getAbsolutePath()));
            } catch (Theme.ThemeUnvalidException e) {
                //TODO: Add exception handling in the UI
            }
        }
    }

    public void setup() {
        if(currentTheme == null) changeThemeTo(0);
    }

    public List<Theme> getThemeList() {
        return themeList;
    }

    public void changeThemeTo(int i) {
        var t = themeList.get(i);
        currentTheme = t;
        onThemeChange.accept(t);
    }

    public Theme getDefaultTheme() {
        return themeList.get(0);
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setOnThemeChange(Consumer<Theme> onThemeChange) {
        this.onThemeChange = onThemeChange;
    }


}
