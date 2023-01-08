package de.uniks.abacus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

@SuppressWarnings({"unused","UnusedReturnValue"})
public class Player
{
   public static final String PROPERTY_RIGHT_SUM = "rightSum";
   public static final String PROPERTY_WRONG_SUM = "wrongSum";
   public static final String PROPERTY_HISTORIES = "histories";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_RESULTS = "results";
   public static final String PROPERTY_ID = "id";
   private int rightSum;
   private int wrongSum;
   private List<History> histories;
   protected PropertyChangeSupport listeners;
   private String name;
   private List<Result> results;
   private int id;

   public int getRightSum()
   {
      return this.rightSum;
   }

   public Player setRightSum(int value)
   {
      if (value == this.rightSum)
      {
         return this;
      }

      final int oldValue = this.rightSum;
      this.rightSum = value;
      this.firePropertyChange(PROPERTY_RIGHT_SUM, oldValue, value);
      return this;
   }

   public int getWrongSum()
   {
      return this.wrongSum;
   }

   public Player setWrongSum(int value)
   {
      if (value == this.wrongSum)
      {
         return this;
      }

      final int oldValue = this.wrongSum;
      this.wrongSum = value;
      this.firePropertyChange(PROPERTY_WRONG_SUM, oldValue, value);
      return this;
   }

   public List<History> getHistories()
   {
      return this.histories != null ? Collections.unmodifiableList(this.histories) : Collections.emptyList();
   }

   public Player withHistories(History value)
   {
      if (this.histories == null)
      {
         this.histories = new ArrayList<>();
      }
      if (!this.histories.contains(value))
      {
         this.histories.add(value);
         value.setPlayer(this);
         this.firePropertyChange(PROPERTY_HISTORIES, null, value);
      }
      return this;
   }

   public Player withHistories(History... value)
   {
      for (final History item : value)
      {
         this.withHistories(item);
      }
      return this;
   }

   public Player withHistories(Collection<? extends History> value)
   {
      for (final History item : value)
      {
         this.withHistories(item);
      }
      return this;
   }

   public Player withoutHistories(History value)
   {
      if (this.histories != null && this.histories.remove(value))
      {
         value.setPlayer(null);
         this.firePropertyChange(PROPERTY_HISTORIES, value, null);
      }
      return this;
   }

   public Player withoutHistories(History... value)
   {
      for (final History item : value)
      {
         this.withoutHistories(item);
      }
      return this;
   }

   public Player withoutHistories(Collection<? extends History> value)
   {
      for (final History item : value)
      {
         this.withoutHistories(item);
      }
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public Player setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public List<Result> getResults()
   {
      return this.results != null ? Collections.unmodifiableList(this.results) : Collections.emptyList();
   }

   public Player withResults(Result value)
   {
      if (this.results == null)
      {
         this.results = new ArrayList<>();
      }
      if (!this.results.contains(value))
      {
         this.results.add(value);
         value.setPlayer(this);
         this.firePropertyChange(PROPERTY_RESULTS, null, value);
      }
      return this;
   }

   public Player withResults(Result... value)
   {
      for (final Result item : value)
      {
         this.withResults(item);
      }
      return this;
   }

   public Player withResults(Collection<? extends Result> value)
   {
      for (final Result item : value)
      {
         this.withResults(item);
      }
      return this;
   }

   public Player withoutResults(Result value)
   {
      if (this.results != null && this.results.remove(value))
      {
         value.setPlayer(null);
         this.firePropertyChange(PROPERTY_RESULTS, value, null);
      }
      return this;
   }

   public Player withoutResults(Result... value)
   {
      for (final Result item : value)
      {
         this.withoutResults(item);
      }
      return this;
   }

   public Player withoutResults(Collection<? extends Result> value)
   {
      for (final Result item : value)
      {
         this.withoutResults(item);
      }
      return this;
   }

   public int getId()
   {
      return this.id;
   }

   public Player setId(int value)
   {
      if (value == this.id)
      {
         return this;
      }

      final int oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_ID, oldValue, value);
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

   public void removeYou()
   {
      this.withoutResults(new ArrayList<>(this.getResults()));
      this.withoutHistories(new ArrayList<>(this.getHistories()));
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      return result.substring(1);
   }
}
