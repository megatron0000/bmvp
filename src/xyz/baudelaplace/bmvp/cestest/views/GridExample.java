package xyz.baudelaplace.bmvp.cestest.views;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.piccolo2d.PCamera;
import org.piccolo2d.PCanvas;
import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.PRoot;
import org.piccolo2d.extras.PFrame;
import org.piccolo2d.util.PPaintContext;


/**
 * Example of drawing an infinite grid, and providing support for snap to grid.
 */
public class GridExample extends PFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected Line2D gridLine = new Line2D.Double();
    protected Stroke gridStroke = new BasicStroke(1);
    protected Color gridPaint = Color.BLACK;
    protected double gridSpacing = 20;
    private boolean isInitialized = false;
    
    public boolean getIsInitialized() {
    	return isInitialized;
    }

    public GridExample() {
        this(null);
        // System.out.println(SwingUtilities.isEventDispatchThread());
    }

    public GridExample(final PCanvas aCanvas) {
        super("GridExample", false, aCanvas);
    }

    @Override
	public void initialize() {
    	// System.out.println(SwingUtilities.isEventDispatchThread());
        final PRoot root = getCanvas().getRoot();
        final PCamera camera = getCanvas().getCamera();
        final PLayer gridLayer = new PLayer() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
			protected void paint(final PPaintContext paintContext) {
                // make sure grid gets drawn on snap to grid boundaries. And
                // expand a little to make sure that entire view is filled.
                final double bx = getX() - getX() % gridSpacing - gridSpacing;
                final double by = getY() - getY() % gridSpacing - gridSpacing;
                final double rightBorder = getX() + getWidth() + gridSpacing;
                final double bottomBorder = getY() + getHeight() + gridSpacing;

                final Graphics2D g2 = paintContext.getGraphics();
                final Rectangle2D clip = paintContext.getLocalClip();

                g2.setStroke(gridStroke);
                g2.setPaint(gridPaint);

                for (double x = bx; x < rightBorder; x += gridSpacing) {
                    gridLine.setLine(x, by, x, bottomBorder);
                    if (clip.intersectsLine(gridLine)) {
                        g2.draw(gridLine);
                    }
                }

                for (double y = by; y < bottomBorder; y += gridSpacing) {
                    gridLine.setLine(bx, y, rightBorder, y);
                    if (clip.intersectsLine(gridLine)) {
                        g2.draw(gridLine);
                    }
                }
            }
        };

        // replace standar layer with grid layer.
        root.removeChild(camera.getLayer(0));
        camera.removeLayer(0);
        root.addChild(gridLayer);
        camera.addLayer(gridLayer);

        // add constrains so that grid layers bounds always match cameras view
        // bounds. This makes it look like an infinite grid.
        camera.addPropertyChangeListener(PNode.PROPERTY_BOUNDS, new PropertyChangeListener() {
            @Override
			public void propertyChange(final PropertyChangeEvent evt) {
                gridLayer.setBounds(camera.getViewBounds());
            }
        });

        camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {
            @Override
			public void propertyChange(final PropertyChangeEvent evt) {
                gridLayer.setBounds(camera.getViewBounds());
            }
        });

        gridLayer.setBounds(camera.getViewBounds());

        final PNode n = new PNode();
        n.setPaint(Color.BLUE);
        n.setBounds(0, 0, 100, 80);

        // getCanvas().getLayer().addChild(n);
        getCanvas().removeInputEventListener(getCanvas().getPanEventHandler());
        
        isInitialized=true;

        // add a drag event handler that supports snap to grid.
//        getCanvas().addInputEventListener(new PDragSequenceEventHandler() {
//
//            protected PNode draggedNode;
//            protected Point2D nodeStartPosition;
//
//            @Override
//			protected boolean shouldStartDragInteraction(final PInputEvent event) {
//                if (super.shouldStartDragInteraction(event)) {
//                    return event.getPickedNode() != event.getTopCamera() && !(event.getPickedNode() instanceof PLayer);
//                }
//                return false;
//            }
//
//            @Override
//			protected void startDrag(final PInputEvent event) {
//                super.startDrag(event);
//                draggedNode = event.getPickedNode();
//                draggedNode.raiseToTop();
//                nodeStartPosition = draggedNode.getOffset();
//            }
//
//            @Override
//			protected void drag(final PInputEvent event) {
//                super.drag(event);
//
//                final Point2D start = getCanvas().getCamera().localToView(
//                        (Point2D) getMousePressedCanvasPoint().clone());
//                final Point2D current = event.getPositionRelativeTo(getCanvas().getLayer());
//                final Point2D dest = new Point2D.Double();
//
//                dest.setLocation(nodeStartPosition.getX() + current.getX() - start.getX(), nodeStartPosition.getY()
//                        + current.getY() - start.getY());
//
//                dest.setLocation(dest.getX() - dest.getX() % gridSpacing, dest.getY() - dest.getY() % gridSpacing);
//
//                draggedNode.setOffset(dest.getX(), dest.getY());
//            }
//        });
    }

    public static void main(final String[] args) {
        new GridExample();
    }
}
