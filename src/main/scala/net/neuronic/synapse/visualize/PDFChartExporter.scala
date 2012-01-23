package net.neuronic.synapse.visualize 

import scala.List
import scala.collection.mutable.ListBuffer
import java.util.{Calendar, Date, Timer}
import java.text.{DateFormat, SimpleDateFormat}

object PDFChartExporter {
  import java.awt.Graphics2D 
  import java.awt.geom.Rectangle2D 
  import java.io.BufferedOutputStream 
  import java.io.File
  import java.io.FileOutputStream 
  import java.io.IOException 
  import java.io.OutputStream 
  import java.text.SimpleDateFormat

  import org.jfree.chart.ChartFactory 
  import org.jfree.chart.JFreeChart 
  import org.jfree.chart.axis.DateAxis 
  import org.jfree.chart.plot.XYPlot 
  import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer 
  import org.jfree.data.time.Month
  import org.jfree.data.time.TimeSeries
  import org.jfree.data.time.TimeSeriesCollection 
  import org.jfree.data.xy.XYDataset

  import com.lowagie.text.Document 
  import com.lowagie.text.DocumentException 
  import com.lowagie.text.Rectangle 
  import com.lowagie.text.pdf.DefaultFontMapper
  import com.lowagie.text.pdf.FontMapper 
  import com.lowagie.text.pdf.PdfContentByte 
  import com.lowagie.text.pdf.PdfTemplate 
  import com.lowagie.text.pdf.PdfWriter
  
  def saveChartAsPDF(file:File, chart:JFreeChart, width:Int, height:Int, mapper:FontMapper): Unit = {
    val out = new BufferedOutputStream(new FileOutputStream(file))
    writeChartAsPDF(out, chart, width, height, mapper)
    out.close
  }

  def writeChartAsPDF(out:OutputStream, chart:JFreeChart, width:Int, height:Int, mapper:FontMapper): Unit = {
    val pagesize = new Rectangle(width, height)
    val document = new Document(pagesize, 50, 50, 50, 50)
    val writer   = PdfWriter.getInstance(document, out)
    document.addAuthor("Neuronic Capital")
    document.addSubject("Demonstration")
    document.open
    val cb  = writer.getDirectContent
    val tp  = cb.createTemplate(width, height)
    val g2  = tp.createGraphics(width, height, mapper)
    val r2D = new Rectangle2D.Double(0, 0, width, height)
    chart.draw(g2, r2D)
    g2.dispose
    cb.addTemplate(tp, 0, 0)
    document.close
  }
}


