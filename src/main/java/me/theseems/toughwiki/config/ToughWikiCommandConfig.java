package me.theseems.toughwiki.config;

import java.util.Set;

public class ToughWikiCommandConfig {
    private Set<String> aliases;
    private Set<String> pages;
    private String permission;

    public ToughWikiCommandConfig() {
    }

    public ToughWikiCommandConfig(Set<String> aliases, Set<String> pages, String permission) {
        this.aliases = aliases;
        this.pages = pages;
        this.permission = permission;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public Set<String> getPages() {
        return pages;
    }

    public void setPages(Set<String> pages) {
        this.pages = pages;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
