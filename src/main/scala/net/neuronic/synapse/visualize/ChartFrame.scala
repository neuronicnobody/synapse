package net.neuronic.synapse.visualize 

import javax.swing.JFrame
import java.awt.{BasicStroke, BorderLayout, GridLayout, Color, Font}
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}

import java.util.Date

import org.jfree.chart.plot.Marker
import org.jfree.chart.annotations.XYAnnotation

import net.neuronic.synapse.data._ 

class ChartFrame {
  private var chart = new TickChart(60000, "Ask", "Bid")
  var frame = new JFrame("sYnapse")
  frame.getContentPane.setLayout( new GridLayout(1, 1) )
  frame.getContentPane.add(chart)

  frame.pack
  frame.setVisible(true)

  def plot(p:PricePoint)  = chart plot p 
  def mark(o:Observation) = chart mark o 
}



