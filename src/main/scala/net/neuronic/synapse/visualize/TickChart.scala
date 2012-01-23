package net.neuronic.synapse.visualize

/* --------------------
 * TickChart.scala
 * --------------------
 * (C) Copyright 2011, by Neuronic Capital, LLC
 */

import java.awt.{BasicStroke, BorderLayout, Color, Font}
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import javax.swing.{BorderFactory, JFrame, JPanel, Timer}

import org.jfree.chart.{ChartPanel, ChartUtilities, JFreeChart}
import org.jfree.chart.axis.{DateAxis, NumberAxis}
import org.jfree.chart.plot.{XYPlot, ValueMarker, IntervalMarker, Marker}
import org.jfree.chart.renderer.xy.{XYItemRenderer, XYLineAndShapeRenderer}
import org.jfree.data.time.{Millisecond, TimeSeries, TimeSeriesCollection}
import org.jfree.chart.annotations.{XYDrawableAnnotation, XYAnnotation}
import org.jfree.ui.{RectangleAnchor, TextAnchor, LengthAdjustmentType, Layer}

import java.util.Date
import java.awt.geom.Ellipse2D

import net.neuronic.synapse.data._ 

class TickChart(val max:Int, val topTitle:String, val botTitle:String) extends JPanel(new BorderLayout) {

  private var top = new TimeSeries(topTitle)
  private var bot = new TimeSeries(botTitle)

  private var dataset = new TimeSeriesCollection 

  private var domain = new DateAxis("Time")
  private var range  = new NumberAxis("Price")

  private var renderer = new XYLineAndShapeRenderer(true, false)
  private var plot = new XYPlot(dataset, domain, range, renderer)

  top.setMaximumItemAge(max)
  bot.setMaximumItemAge(max)

  dataset addSeries top
  dataset addSeries bot

  renderer.setSeriesPaint(0, Color.black)
  renderer.setSeriesPaint(1, Color.black)
  renderer.setSeriesShapesVisible(0, true)
  renderer.setSeriesShapesVisible(1, true)
  renderer.setShapesFilled(true)
  renderer.setSeriesShape(0, new Ellipse2D.Double(-2, -2, 4, 4))
  renderer.setSeriesShape(1, new Ellipse2D.Double(-2, -2, 4, 4))

  plot.setDomainCrosshairVisible(true)
  plot.setRangeCrosshairVisible(true)
  plot.setForegroundAlpha(0.5f)

  range.setAutoRange(true)
  range.setAutoRangeIncludesZero(false)

  domain.setAutoRange(true)
  domain.setLowerMargin(0.0)
  domain.setUpperMargin(0.0)
  domain.setTickLabelsVisible(true)

  var chart = new JFreeChart("", new Font("SansSerif", Font.BOLD, 24), plot, true)
  var chartPanel = new ChartPanel(chart, true)
  add(chartPanel);

  def plot(p:PricePoint) = {
    p.series match {
      case 'Ask => this.top.addOrUpdate(new Millisecond(p.rec), p.price)
      case 'Bid => this.bot.addOrUpdate(new Millisecond(p.rec), p.price)
    }
  }

  def mark(obs:Observation) = {
    obs match {
      case p:PricePoint => addAnnotation(p) 
      case d:Divergence => for(p <- d.getPricePoints) addAnnotation(p)
      case d:PriceSegment => for(p <- d.getPricePoints) addAnnotation(p)
      case w:PriceWave => for(p <- w.getPricePoints) addAnnotation(p)
    }
  }

  private def addAnnotation(p:PricePoint) = {
    val m  = new Millisecond(p.rec)
    val cd = new CircleDrawer(Color.blue, new BasicStroke(1.0f), null)
    val marker = new XYDrawableAnnotation(m.getFirstMillisecond(), p.price, 11, 11, cd)
    plot.addAnnotation(marker)
  }

  private def deleteAnnotation(a:XYAnnotation): Unit = {
    plot.removeAnnotation(a);
  }

  private def deleteThresh(m:Marker): Unit = {
    plot.removeRangeMarker(m);
  }
}

