package pt.webdetails.cda.dataaccess;

import javax.swing.table.TableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import pt.webdetails.cda.settings.CdaSettings;

/**
 * This is the top level implementation of a DataAccess. Only the common methods are used here
 * <p/>
 * User: pedro
 * Date: Feb 3, 2010
 * Time: 11:05:38 AM
 */
public abstract class AbstractDataAccess implements DataAccess
{

  private static final Log logger = LogFactory.getLog(AbstractDataAccess.class);

  private CdaSettings cdaSettings;
  private String id;
  private DataAccessEnums.ACCESS_TYPE access = DataAccessEnums.ACCESS_TYPE.PUBLIC;
  private boolean cache = false;
  private int cacheDuration = 3600;


  protected AbstractDataAccess(final Element element)
  {

    id = element.attributeValue("id");

    if (element.attributeValue("access") != null && element.attributeValue("access").equals("private"))
    {
      access = DataAccessEnums.ACCESS_TYPE.PRIVATE;
    }

    if (element.attributeValue("cache") != null && element.attributeValue("cache").equals("true"))
    {
      cache = true;
    }

    if (element.attribute("cacheDuration") != null)
    {
      cacheDuration = Integer.parseInt(element.attributeValue("cacheDuration"));
    }

  }

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public abstract TableModel queryData() throws QueryException;


  public DataAccessEnums.ACCESS_TYPE getAccess()
  {
    return access;
  }

  public boolean isCache()
  {
    return cache;
  }

  public int getCacheDuration()
  {
    return cacheDuration;
  }

  public CdaSettings getCdaSettings()
  {
    return cdaSettings;
  }

  public void setCdaSettings(CdaSettings cdaSettings)
  {
    this.cdaSettings = cdaSettings;
  }

  protected static TableModel copyTableModel(TableModel t)
  {
    final int count = t.getColumnCount();
    final Class[] colTypes = new Class[count];
    final String[] colNames = new String[count];
    for (int i = 0; i < count; i++)
    {
      colTypes[i] = t.getColumnClass(i);
      colNames[i] = t.getColumnName(i);
    }
    final int rowCount = t.getRowCount();
    final TypedTableModel typedTableModel = new TypedTableModel(colNames, colTypes, rowCount);
    for (int r = 0; r < rowCount; r++)
    {
      for (int c = 0; c < count; c++)
      {
        typedTableModel.setValueAt(t.getValueAt(r, c), r, c);
      }
    }
    return typedTableModel;
  }

}