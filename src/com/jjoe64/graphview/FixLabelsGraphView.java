package com.jjoe64.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;


/**
 * GraphView is a Android View for creating zoomable and scrollable graphs.
 * FixLabelsGraphView draws the specified horizontal labels without automatically 
 *   generating the labels.
 * The labels are specified using the graph values and text labels.
 * if labels are not set, it behaves the same as GraphView.
 *
 * Client can do custom drawing by setting {@link IDrawGraph}.
 *
 * @author skyleecm - 
 *
 * Copyright (C) 2013 
 * Licensed under the GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 */
abstract public class FixLabelsGraphView extends GraphView {

    public static class FixLabel {
        public double value;
        public String label;

        public FixLabel(double value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    protected FixLabel[] horlabels;

    private IDrawGraph iDraw;

    // parameters in drawHorizontalLabelsLines
    // save them for use in iDraw.drawAfterSeries
    private float drawGraphwidth;
    private float drawGraphheight; 
    private float drawBorder; 
    private float drawHorstart;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;



    public FixLabelsGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixLabelsGraphView(Context context, String title) {
        super(context, title);
    }


    // save parameters for use in iDraw.drawAfterSeries
    private void setDrawParams(float graphwidth, float graphheight, 
            float border, float horstart,
            double minX, double maxX, double minY, double maxY) {
        drawGraphwidth = graphwidth;
        drawGraphheight = graphheight;
        drawBorder = border;
        drawHorstart = horstart;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    protected void drawHorizontalLabelsLines(Canvas canvas, float graphwidth,
            float graphheight, float border, float horstart) {
        double maxY = getMaxY();
        double minY = getMinY();
        double maxX = getMaxX(false);
        double minX = getMinX(false);
        setDrawParams(graphwidth, graphheight, border, horstart,
                minX, maxX, minY, maxY);
        if (horlabels == null) {
            super.drawHorizontalLabelsLines(canvas, graphwidth, graphheight,
                    border, horstart);
            if (iDraw != null)
                iDraw.drawBeforeSeries(canvas, graphwidth, graphheight,
                    border, horstart, minX, maxX, minY, maxY);
            return;
        } 
        GraphViewStyle graphViewStyle = getGraphViewStyle();
        final float halfLabelSize = GraphViewConfig.VERTICAL_LABEL_WIDTH / 2; 
        float height = graphheight + (2 * border); 
        double min = minX;
        double max = maxX;
        double diff = max - min;
        for (int i = 0; i < horlabels.length; i++) {
            double value = horlabels[i].value;
            if (value > max)
                break;
            else if (value < min)
                continue;
            paint.setColor(graphViewStyle.getGridColor());
            float x = (float) ((value - min)/diff) * graphwidth + horstart;
            canvas.drawLine(x, height - border, x, border, paint);
            paint.setTextAlign(Align.CENTER);
            if (x + halfLabelSize > graphwidth)
                paint.setTextAlign(Align.RIGHT);
            else
                paint.setTextAlign(Align.LEFT);
            paint.setColor(graphViewStyle.getHorizontalLabelsColor());
            canvas.drawText(horlabels[i].label, x, height - 4, paint);
        }
        if (iDraw != null)
            iDraw.drawBeforeSeries(canvas, graphwidth, graphheight,
                border, horstart, minX, maxX, minY, maxY);
    }

    // using this to call IDrawGraph.drawAfterSeries
    @Override
    protected void drawLegend(Canvas canvas, float height, float width) {
        if (iDraw != null)
            iDraw.drawAfterSeries(canvas, drawGraphwidth, drawGraphheight,
                    drawBorder, drawHorstart, minX, maxX, minY, maxY);
        // we have modified parent drawLegend (always called)
        super.drawLegend(canvas, height, width);
    }


	/**
	 * returns the maximal X value of the current viewport (if viewport is set)
	 * otherwise maximal X value of all data.
	 * @param ignoreViewport
	 *
	 * warning: only override this, if you really know want you're doing!
	 */
    @Override 
    protected double getMaxX(boolean ignoreViewport) { 
        double max = super.getMaxX(ignoreViewport);
        double viewportSize = getViewPortSize(); 
        // if viewport is set, use this 
        if (!ignoreViewport && viewportSize != 0) { 
            return max; 
        } else { 
            // otherwise use the max x value and horlabels
            if (horlabels != null) 
                return Math.max(max, horlabels[horlabels.length-1].value); 
            return max; 
        } 
    }

	/**
	 * returns the minimal X value of the current viewport (if viewport is set)
	 * otherwise minimal X value of all data.
	 * @param ignoreViewport
	 *
	 * warning: only override this, if you really know want you're doing!
	 */
    @Override
    protected double getMinX(boolean ignoreViewport) { 
        double min = super.getMinX(ignoreViewport);
        double viewportSize = getViewPortSize(); 
        // if viewport is set, use this 
        if (!ignoreViewport && viewportSize != 0) { 
            return min; 
        } else { 
            // otherwise use the min x value and horlabels
            if (horlabels != null) 
                return Math.min(min, horlabels[0].value);
            return min; 
        } 
    }


    /**
    * set's static horizontal labels (from left to right)
    * @param horlabels 
    */ 
    public void setHorizontalLabels(FixLabel[] horlabels) { 
        this.horlabels = horlabels; 
    }

    /**
    * returns static horizontal labels
    */ 
    public FixLabel[] getHorizontalLabels() { 
        return horlabels; 
    }

    /**
     * set's client IDrawGraph
     * @param drawGraph
     */
    public void setDrawGraph(IDrawGraph drawGraph) {
        iDraw = drawGraph;
    }

    /**
     * returns client IDrawGraph
     */
    public IDrawGraph getDrawGraph() {
        return iDraw;
    }

}
