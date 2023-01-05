package de.uniks.abacus.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

@SuppressWarnings({"unused","UnusedReturnValue"})
public class Result
{
   public static final String PROPERTY_RESULT_STATUS = "resultStatus";
   public static final String PROPERTY_OPERATION = "operation";
   public static final String PROPERTY_FIRST_VAL = "firstVal";
   public static final String PROPERTY_SECOND_VAL = "secondVal";
   public static final String PROPERTY_RESULT_VAL = "resultVal";
   public static final String PROPERTY_RIGHT_VAL = "rightVal";
   public static final String PROPERTY_HISTORY = "history";
   public static final String PROPERTY_PLAYER = "player";
   private String resultStatus;
   private char operation;
   private int firstVal;
   private int secondVal;
   private int resultVal;
   private int rightVal;
   protected PropertyChangeSupport listeners;
   private History history;
   private Player player;

   public String getResultStatus()
   {
      return this.resultStatus;
   }

   public Result setResultStatus(String value)
   {
      if (Objects.equals(value, this.resultStatus))
      {
         return this;
      }

      final String oldValue = this.resultStatus;
      this.resultStatus = value;
      this.firePropertyChange(PROPERTY_RESULT_STATUS, oldValue, value);
      return this;
   }

   public char getOperation()
   {
      return this.operation;
   }

   public Result setOperation(char value)
   {
      if (value == this.operation)
      {
         return this;
      }

      final char oldValue = this.operation;
      this.operation = value;
      this.firePropertyChange(PROPERTY_OPERATION, oldValue, value);
      return this;
   }

   public int getFirstVal()
   {
      return this.firstVal;
   }

   public Result setFirstVal(int value)
   {
      if (value == this.firstVal)
      {
         return this;
      }

      final int oldValue = this.firstVal;
      this.firstVal = value;
      this.firePropertyChange(PROPERTY_FIRST_VAL, oldValue, value);
      return this;
   }

   public int getSecondVal()
   {
      return this.secondVal;
   }

   public Result setSecondVal(int value)
   {
      if (value == this.secondVal)
      {
         return this;
      }

      final int oldValue = this.secondVal;
      this.secondVal = value;
      this.firePropertyChange(PROPERTY_SECOND_VAL, oldValue, value);
      return this;
   }

   public int getResultVal()
   {
      return this.resultVal;
   }

   public Result setResultVal(int value)
   {
      if (value == this.resultVal)
      {
         return this;
      }

      final int oldValue = this.resultVal;
      this.resultVal = value;
      this.firePropertyChange(PROPERTY_RESULT_VAL, oldValue, value);
      return this;
   }

   public int getRightVal()
   {
      return this.rightVal;
   }

   public Result setRightVal(int value)
   {
      if (value == this.rightVal)
      {
         return this;
      }

      final int oldValue = this.rightVal;
      this.rightVal = value;
      this.firePropertyChange(PROPERTY_RIGHT_VAL, oldValue, value);
      return this;
   }

   public History getHistory()
   {
      return this.history;
   }

   public Result setHistory(History value)
   {
      if (this.history == value)
      {
         return this;
      }

      final History oldValue = this.history;
      if (this.history != null)
      {
         this.history = null;
         oldValue.withoutResults(this);
      }
      this.history = value;
      if (value != null)
      {
         value.withResults(this);
      }
      this.firePropertyChange(PROPERTY_HISTORY, oldValue, value);
      return this;
   }

   public Player getPlayer()
   {
      return this.player;
   }

   public Result setPlayer(Player value)
   {
      if (this.player == value)
      {
         return this;
      }

      final Player oldValue = this.player;
      if (this.player != null)
      {
         this.player = null;
         oldValue.withoutResults(this);
      }
      this.player = value;
      if (value != null)
      {
         value.withResults(this);
      }
      this.firePropertyChange(PROPERTY_PLAYER, oldValue, value);
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
      result.append(' ').append(this.getResultStatus());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setHistory(null);
      this.setPlayer(null);
   }
}
