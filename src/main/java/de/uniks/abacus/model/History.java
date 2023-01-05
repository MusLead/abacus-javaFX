package de.uniks.abacus.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class History
{
   public static final String PROPERTY_RIGHT_RESULT_TOTAL = "rightResultTotal";
   public static final String PROPERTY_WRONG_RESULT_TOTAL = "wrongResultTotal";
   public static final String PROPERTY_TIME = "time";
   public static final String PROPERTY_FINISH = "finish";
   public static final String PROPERTY_PLAYER = "player";
   public static final String PROPERTY_RESULTS = "results";
   private int rightResultTotal;
   private int wrongResultTotal;
   private String time;
   private String finish;
   private Player player;
   protected PropertyChangeSupport listeners;
   private List<Result> results;

   public int getRightResultTotal()
   {
      return this.rightResultTotal;
   }

   public History setRightResultTotal(int value)
   {
      if (value == this.rightResultTotal)
      {
         return this;
      }

      final int oldValue = this.rightResultTotal;
      this.rightResultTotal = value;
      this.firePropertyChange(PROPERTY_RIGHT_RESULT_TOTAL, oldValue, value);
      return this;
   }

   public int getWrongResultTotal()
   {
      return this.wrongResultTotal;
   }

   public History setWrongResultTotal(int value)
   {
      if (value == this.wrongResultTotal)
      {
         return this;
      }

      final int oldValue = this.wrongResultTotal;
      this.wrongResultTotal = value;
      this.firePropertyChange(PROPERTY_WRONG_RESULT_TOTAL, oldValue, value);
      return this;
   }

   public String getTime()
   {
      return this.time;
   }

   public History setTime(String value)
   {
      if (Objects.equals(value, this.time))
      {
         return this;
      }

      final String oldValue = this.time;
      this.time = value;
      this.firePropertyChange(PROPERTY_TIME, oldValue, value);
      return this;
   }

   public String getFinish()
   {
      return this.finish;
   }

   public History setFinish(String value)
   {
      if (Objects.equals(value, this.finish))
      {
         return this;
      }

      final String oldValue = this.finish;
      this.finish = value;
      this.firePropertyChange(PROPERTY_FINISH, oldValue, value);
      return this;
   }

   public Player getPlayer()
   {
      return this.player;
   }

   public History setPlayer(Player value)
   {
      if (this.player == value)
      {
         return this;
      }

      final Player oldValue = this.player;
      if (this.player != null)
      {
         this.player = null;
         oldValue.withoutHistories(this);
      }
      this.player = value;
      if (value != null)
      {
         value.withHistories(this);
      }
      this.firePropertyChange(PROPERTY_PLAYER, oldValue, value);
      return this;
   }

   public List<Result> getResults()
   {
      return this.results != null ? Collections.unmodifiableList(this.results) : Collections.emptyList();
   }

   public History withResults(Result value)
   {
      if (this.results == null)
      {
         this.results = new ArrayList<>();
      }
      if (!this.results.contains(value))
      {
         this.results.add(value);
         value.setHistory(this);
         this.firePropertyChange(PROPERTY_RESULTS, null, value);
      }
      return this;
   }

   public History withResults(Result... value)
   {
      for (final Result item : value)
      {
         this.withResults(item);
      }
      return this;
   }

   public History withResults(Collection<? extends Result> value)
   {
      for (final Result item : value)
      {
         this.withResults(item);
      }
      return this;
   }

   public History withoutResults(Result value)
   {
      if (this.results != null && this.results.remove(value))
      {
         value.setHistory(null);
         this.firePropertyChange(PROPERTY_RESULTS, value, null);
      }
      return this;
   }

   public History withoutResults(Result... value)
   {
      for (final Result item : value)
      {
         this.withoutResults(item);
      }
      return this;
   }

   public History withoutResults(Collection<? extends Result> value)
   {
      for (final Result item : value)
      {
         this.withoutResults(item);
      }
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getTime());
      result.append(' ').append(this.getFinish());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutResults(new ArrayList<>(this.getResults()));
      this.setPlayer(null);
   }
}
