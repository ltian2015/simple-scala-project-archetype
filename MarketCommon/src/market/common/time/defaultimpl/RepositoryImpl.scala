package market.common.time.defaultimpl
import market.common.time._
import market.common._
import scala.collection.mutable.ListBuffer;
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import market.common.time.defaultimpl._

class RepositoryImpl(val calendar:MktCalendar) extends MktCalendarRepository{
  val tcConfigUrl:java.net.URL=this.getClass.getResource("/resource/TradeCenterConfig.xml");
  def  loadRegisteredIntervalTypes:List[MarketIntervalType]={
    val tradeCenterId=this.calendar.tradeCenterId;
    val itvTypeConfigUrl=this.getClass.getResource("/resource/MarketIntervalTypeConfig."+tradeCenterId+".xml");
    val rootNode=scala.xml.XML.load(itvTypeConfigUrl);
    val listBuffer=new ListBuffer[MarketIntervalType]();
    println("load interval type..............................");
    for(node <- (rootNode \ "MarketIntervalType")){
            val originTimeText=(node \ "@originTime").text;
            val startTime:LocalDateTime=LocalDateTime.parse(originTimeText,DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            val isStop=(node \ "@isStop").text.toBoolean;
            val stopTimeText=(node \ "@stopTime").text;
            val stopTime:LocalDateTime=if (stopTimeText=="" || stopTimeText=="-") null 
                 else LocalDateTime.parse(stopTimeText,DateTimeFormatter.ISO_LOCAL_DATE_TIME);  
            val typeId=(node \ "@id").text;
            val intervalUnit=MarketIntervalUnit.from((node \ "@intervalUnit").text);
            val unitCount= (node \ "@unitCount").text.toInt;
            val itvType:MarketIntervalType=new DefaultIntervalType(typeId,
                         calendar, unitCount, intervalUnit,startTime,isStop,stopTime);
            listBuffer += itvType;
    }
    listBuffer.toList;
  }
  def  saveNewIntervalType(itvType:MarketIntervalType):Unit={
     //TODO
  }
  def  updateIntervalType(itvType:MarketIntervalType):Unit={
     //TODO
  }
  
  def loadTradeCenter:TradeCenter={
     println("load tradeCenter..............................");
    val tradeCenterId:String=this.calendar.tradeCenterId;
    val rootNode=scala.xml.XML.load(tcConfigUrl);
    val listBuffer=new ListBuffer[TradeCenter]();
    for(node <- (rootNode \ "TradeCenter")){
       if ((node \@"id")==tradeCenterId)
       {
        val tc:TradeCenter=BaseTradeCenter( (node \ "@id").text ,
                              (node \ "@name").text,
                              (node \ "@description").text );
        listBuffer += tc;
       }
    }
    if (listBuffer.isEmpty) 
       throw new Exception("")
    else if (listBuffer.size>1)
        throw new Exception("");
    else listBuffer(0);
  }

}