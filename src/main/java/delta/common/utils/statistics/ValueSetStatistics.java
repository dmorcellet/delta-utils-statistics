package delta.common.utils.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.misc.IntegerHolder;
import delta.common.utils.text.EndOfLine;

/**
 * Statistics on a set of values
 * @author DAM
 */
public class ValueSetStatistics
{
  private Map<Integer,IntegerHolder> _stats;
  private int _totalSamples;

  /**
   * Constructor.
   */
  public ValueSetStatistics()
  {
    _stats=new HashMap<Integer,IntegerHolder>();
  }

  /**
   * Add a value.
   * @param value Value to add.
   */
  public void addValue(int value)
  {
    Integer key=Integer.valueOf(value);
    IntegerHolder h=_stats.get(key);
    if (h==null)
    {
      h=new IntegerHolder();
      _stats.put(key,h);
    }
    h.increment();
    _totalSamples++;
  }

  /**
   * Get the number of different values.
   * @return A count.
   */
  public int getValuesCount()
  {
    return _stats.size();
  }

  /**
   * Get the total number of registered samples.
   * @return A count.
   */
  public int getTotalSamples()
  {
    return _totalSamples;
  }

  /**
   * Get the number of samples for the given value.
   * @param value Value to use.
   * @return A count.
   */
  public int getCountForValue(int value)
  {
    IntegerHolder c=_stats.get(Integer.valueOf(value));
    return (c!=null)?c.getInt():0;
  }

  /**
   * Get the values, sorted by increasing occurrence count.
   * @return A list of values.
   */
  public List<Integer> getValuesSortedByOccurrence()
  {
    List<Integer> ret=new ArrayList<Integer>(_stats.keySet());
    Collections.sort(ret,getOccurrenceComparator());
    return ret;
  }

  /**
   * Get the values, sorted by increasing value.
   * @return A list of values.
   */
  public List<Integer> getSortedValues()
  {
    List<Integer> ret=new ArrayList<Integer>(_stats.keySet());
    Collections.sort(ret);
    return ret;
  }

  private Comparator<Integer> getOccurrenceComparator()
  {
    Comparator<Integer> c=new Comparator<Integer>()
    {
      public int compare(Integer v1, Integer v2)
      {
        IntegerHolder count1=_stats.get(v1);
        IntegerHolder count2=_stats.get(v2);
        return Integer.compare(count1.getInt(),count2.getInt());
      }
    };
    return c;
  }

  /**
   * Get the percentage for the given value.
   * @param value Value to use.
   * @return A percentage (0-100).
   */
  public float getPercentage(int value)
  {
    int count=getCountForValue(value);
    int totalSamples=getTotalSamples();
    float percentage=(count*100f)/totalSamples;
    return percentage;
  }

  /**
   * Dump an occurrences table as a string.
   * @return a displayable string.
   */
  public String dumpByOccurrence()
  {
    List<Integer> values=getValuesSortedByOccurrence();
    Collections.reverse(values);
    return dump(values);
  }

  /**
   * Dump a values table as a string.
   * @return a displayable string.
   */
  public String dumpByValue()
  {
    List<Integer> values=getSortedValues();
    return dump(values);
  }

  private String dump(List<Integer> values)
  {
    StringBuilder sb=new StringBuilder();
    for(Integer value : values)
    {
      int count=getCountForValue(value.intValue());
      float percentage=getPercentage(value.intValue());
      sb.append(value).append(" => ").append(count).append(" (").append(percentage).append("%)").append(EndOfLine.NATIVE_EOL);
    }
    return sb.toString();
  }
}
