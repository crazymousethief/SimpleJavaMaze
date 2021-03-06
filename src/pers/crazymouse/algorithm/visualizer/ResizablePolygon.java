package pers.crazymouse.algorithm.visualizer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.shape.Polygon;

/**
 * Created by crazymouse on 6/21/16.
 */
public class ResizablePolygon extends Polygon implements Resizable {
    private double currentWidth;
    private double currentHeight;

    DoubleProperty resizableWidth = new SimpleDoubleProperty();
    DoubleProperty resizableHeight = new SimpleDoubleProperty();

    public ResizablePolygon() {
        resizableWidth.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                resize(getResizableHeight().getValue(), resizableHeight.getValue());
            }
        });
        resizableHeight.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                resize(getResizableHeight().getValue(), resizableHeight.getValue());
            }
        });
    }

    public void resize() {
        resize(resizableWidth.getValue(), resizableHeight.getValue());
    }

    @Override
    public void resize(double width, double height) {
        computeSize();
        if (width == 0 || height == 0) return;
        resizeWithRate(width / currentWidth, height / currentHeight);
    }

    public void resizeWithRate(double rate) {
        resizeWithRate(rate, rate);
    }

    public void resizeWithRate(double zoomWidthRate, double zoomHeightRate) {
        for (int i = 0; i < getPoints().size(); i += 2) {
            double w = getPoints().get(i);
            double h = getPoints().get(i + 1);
            getPoints().set(i, w * zoomWidthRate);
            getPoints().set(i + 1, h * zoomHeightRate);
        }
    }

    double boundedSize(double value, double min, double max) {
        // if max < value, return max
        // if min > value, return min
        // if min > max, return min
        return Math.min(Math.max(value, min), Math.max(min, max));
    }

    public void computeSize() {
        Orientation contentBias = getContentBias();
        double w, h;
        if (contentBias == null) {
            w = boundedSize(prefWidth(-1), minWidth(-1), maxWidth(-1));
            h = boundedSize(prefHeight(-1), minHeight(-1), maxHeight(-1));
        } else if (contentBias == Orientation.HORIZONTAL) {
            w = boundedSize(prefWidth(-1), minWidth(-1), maxWidth(-1));
            h = boundedSize(prefHeight(w), minHeight(w), maxHeight(w));
        } else {
            h = boundedSize(prefHeight(-1), minHeight(-1), maxHeight(-1));
            w = boundedSize(prefWidth(h), minWidth(h), maxWidth(h));
        }
        currentWidth = w;
        currentHeight = h;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public DoubleProperty getResizableWidth() {
        return resizableWidth;
    }

    @Override
    public DoubleProperty getResizableHeight() {
        return resizableHeight;
    }
}
