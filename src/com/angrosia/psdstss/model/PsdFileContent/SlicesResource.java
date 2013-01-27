package com.angrosia.psdstss.model.PsdFileContent;

import java.util.ArrayList;
import java.util.List;

public class SlicesResource {
    private Integer version;
    private Integer top;
    private Integer left;
    private Integer bottom;
    private Integer right;
    private String name;
    private List<Slice> slices = new ArrayList<Slice>();

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getBottom() {
        return bottom;
    }

    public void setBottom(Integer bottom) {
        this.bottom = bottom;
    }

    public Integer getRight() {
        return right;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Slice> getSlices() {
        return slices;
    }

    public void setSlices(List<Slice> slices) {
        this.slices = slices;
    }

    public void addSlice(Slice slice) {
        this.slices.add(slice);
    }
}
