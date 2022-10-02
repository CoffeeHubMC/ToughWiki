package me.theseems.toughwiki.impl;

import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.WikiPageInfo;

import java.util.Collection;
import java.util.Optional;

public class SimpleWikiPage implements WikiPage {
    private final String name;
    private WikiPage parent;
    private Collection<WikiPage> children;
    private WikiPageInfo info;

    public SimpleWikiPage(String name, WikiPage parent, Collection<WikiPage> children, WikiPageInfo info) {
        this.name = name;
        this.parent = parent;
        this.children = children;
        this.info = info;
    }

    @Override
    public Optional<WikiPage> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(WikiPage parent) {
        this.parent = parent;
    }

    @Override
    public Collection<WikiPage> getChildren() {
        return children;
    }

    public void setChildren(Collection<WikiPage> children) {
        this.children = children;
    }

    @Override
    public WikiPageInfo getInfo() {
        return info;
    }

    public void setInfo(WikiPageInfo info) {
        this.info = info;
    }

    @Override
    public String getName() {
        return name;
    }


}
