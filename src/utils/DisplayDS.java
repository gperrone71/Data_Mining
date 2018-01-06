/**
 * 
 */
package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import java.util.ArrayList;

import objects.*;

/**
 * Very rude class for displaying thw world and the results of the simulation
 * 
 * @author Giovanni
 *
 */

public class DisplayDS extends JComponent {
       
    class NodesConnection {
    	private Resource res;
    	private Task tskO;
    	private Task tskE;
		public Resource getRes() {
			return res;
		}
		public void setRes(Resource res) {
			this.res = res;
		}
		public Task getTskO() {
			return tskO;
		}
		public void setTskO(Task tskO) {
			this.tskO = tskO;
		}
		public Task getTskE() {
			return tskE;
		}
		public void setTskE(Task tskE) {
			this.tskE = tskE;
		}
    	
    }
    
    private ArrayList<Task> lstNodes1;
    private ArrayList<NodesConnection> lstConn;
    
    public DisplayDS(int width, int height) {
        super();
        setPreferredSize(new Dimension(width,height));
        lstNodes1 = new ArrayList<Task>();
        lstConn = new ArrayList<NodesConnection>();
    }

    /**
     * addNode
     * 
     * Public method to add nodes to be drawn 
     * 
     * @param tsk	the node to be added
     */
    public void addNode(Task tsk) {
    	lstNodes1.add(tsk);
//    	PerroUtils.print("Task added with " +tsk.getNode().getLatitude() + "lat and " + tsk.getNode().getLongitude() + " lon");
//    	repaint();
    }
    
    /**
     * addLine
     * 
     * Public method to add lines to be drawn 
     * 
     * @param res	the resource for which we want to draw the line
     * @param tskO	the origin node
     * @param tskE	the end node
     */
    public void addLine(Resource res, Task tskO, Task tskE) {
    	NodesConnection tmpConn = new NodesConnection();
    	tmpConn.setRes(res);
    	tmpConn.setTskO(tskO);
    	tmpConn.setTskE(tskE);
    	lstConn.add(tmpConn);
    }

    
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        
        // find scaling factor for x and y
        double maxX = -100000;
        double maxY = -100000;
        double minX = 100000;
        double minY = 100000;
        
        for (Task tsk : lstNodes1) {
        	double lat = tsk.getNode().getLatitude();
        	double lon = tsk.getNode().getLongitude();
        	
        	maxX = (maxX <= lat) ? lat : maxX;
        	minX = (minX >= lat) ? lat : minX;
        	
        	maxY = (maxY <= lon) ? lon : maxY;       	
        	minY = (minY >= lon) ? lon : minY;
        }
        
        double scaleX = 1000/(maxX - minX);
        double scaleY = 1000/(maxY - minY);

        ArrayList<Task> lstNodes_tmp = new ArrayList<Task>();

        // create a new list with with all nodes with the scaling factors
        for (Task tsk : lstNodes1) {
        	Node nd = new Node();
        	nd = tsk.getNode();
        	nd.setLatitude((nd.getLatitude() - minX) * scaleX);
        	nd.setLongitude((nd.getLongitude() - minY) * scaleY);
        	tsk.setNode(nd);

        	PerroUtils.print("new coord: lat "+ nd.getLatitude() + " lon "+ nd.getLongitude());

        	lstNodes_tmp.add(tsk);       	
        }
        
        PerroUtils.print("---");
        	
        // drawing nodes first
        for (Task tsk : lstNodes_tmp) {
        	int lat = 0;
        	int lon = 0;
        	
        	lat = (int) tsk.getNode().getLatitude();
        	lon = (int) tsk.getNode().getLongitude();
        	
        	PerroUtils.print("drawing :lat "+ lat + " lon "+ lon);
        	g.drawOval(
        			lat, 
        			lon, 
        			3, 
        			3);
            g.drawString(tsk.getType()+" "+tsk.getId(), lat ,lon);
        }
        
        // drawing connections
/*        
        g.setColor(Color.red);
        for (NodesConnection cnt : lstConn) {
            double xPos = (cnt.getTskO().getNode().getLatitude() + cnt.getTskE().getNode().getLatitude() )/ 2;
            double yPos = (cnt.getTskO().getNode().getLongitude() + cnt.getTskE().getNode().getLongitude() ) / 2;

            g.drawLine(
                    (int)cnt.getTskO().getNode().getLatitude()*10,
                    (int)cnt.getTskO().getNode().getLongitude()*10,
                    (int)cnt.getTskE().getNode().getLatitude()*10,
                    (int)cnt.getTskE().getNode().getLongitude()*10 );
            
            g.drawString("R"+cnt.res.getId(),
            		(int)xPos*10,
                    (int)yPos*10);
        }
  */      

    }
}