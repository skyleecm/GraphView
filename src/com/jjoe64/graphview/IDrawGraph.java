package com.jjoe64.graphview;

import android.graphics.Canvas;

/**
 * This allows client to draw something on the graph
 *  when GraphView.onDraw is called.
 * The first one is called after the labels and background lines are drawn,
 *  after the method drawHorizontalLabelsLines.
 * The second is called after the graph series are drawn.
 *
 * @author skyleecm - 
 *
 * Copyright (C) 2013 
 * Licensed under the GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 */

public interface IDrawGraph {

    public void drawBeforeSeries(Canvas canvas, float graphwidth,
            float graphheight, float border, float horstart,
            double minX, double maxX, double minY, double maxY);
    
    public void drawAfterSeries(Canvas canvas, float graphwidth,
            float graphheight, float border, float horstart,
            double minX, double maxX, double minY, double maxY);

}
