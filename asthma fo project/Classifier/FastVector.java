package Classifier;

import java.util.*;
import java.io.*;
/**
 * Implements a fast vector class without synchronized
 * methods. Replaces java.util.Vector. (Synchronized methods tend to
 * be slow.)
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.11 $ */
public class FastVector {//implements Copyable, Serializable {

  /**
   * Class for enumerating the vector's elements.
   */
  public class FastVectorEnumeration implements Enumeration {

    /** The counter. */
    private int m_Counter;
    // These JML commands say how m_Counter implements Enumeration
    //@ in moreElements;
    //@ private represents moreElements = m_Counter < m_Vector.size();
    //@ private invariant 0 <= m_Counter && m_Counter <= m_Vector.size();

    /** The vector. */
    private /*@non_null@*/ FastVector m_Vector;

    /** Special element. Skipped during enumeration. */
    private int m_SpecialElement;
    //@ private invariant -1 <= m_SpecialElement;
    //@ private invariant m_SpecialElement < m_Vector.size();
    //@ private invariant m_SpecialElement>=0 ==> m_Counter!=m_SpecialElement;

    /**
     * Constructs an enumeration.
     *
     * @param vector the vector which is to be enumerated
     */
    public FastVectorEnumeration(/*@non_null@*/FastVector vector) {

      m_Counter = 0;
      m_Vector = vector;
      m_SpecialElement = -1;
    }

    /**
     * Constructs an enumeration with a special element.
     * The special element is skipped during the enumeration.
     *
     * @param vector the vector which is to be enumerated
     * @param special the index of the special element
     */
    //@ requires 0 <= special && special < vector.size();
    public FastVectorEnumeration(/*@non_null@*/FastVector vector, int special){

      m_Vector = vector;
      m_SpecialElement = special;
      if (special == 0) {
        m_Counter = 1;
      } else {
        m_Counter = 0;
      }
    }


    /**
     * Tests if there are any more elements to enumerate.
     *
     * @return true if there are some elements left
     */
    public final /*@pure@*/ boolean hasMoreElements() {

      if (m_Counter < m_Vector.size()) {
        return true;
      }
      return false;
    }

    /**
     * Returns the next element.
     *
     * @return the next element to be enumerated
     */
    //@ also requires hasMoreElements();
    
    public final Object nextElement() {

      Object result = m_Vector.elementAt(m_Counter);

      m_Counter++;
      if (m_Counter == m_SpecialElement) {
        m_Counter++;
      }
      return result;
    }
  }

  /** The array of objects. */
  private /*@spec_public@*/  String[] m_Objects;//char[][]
  //@ invariant m_Objects != null;
  //@ invariant m_Objects.length >= 0;

  /** The current size; */
  private /*@spec_public@*/ int m_Size = 0;
  //@ invariant 0 <= m_Size;
  //@ invariant m_Size <= m_Objects.length;

  /** The capacity increment */
  private /*@spec_public@*/ int m_CapacityIncrement = 1;
  //@ invariant 1 <= m_CapacityIncrement;

  /** The capacity multiplier. */
  private /*@spec_public@*/ int m_CapacityMultiplier = 2;
  //@ invariant 1 <= m_CapacityMultiplier;

  // Make sure the size will increase...
  //@ invariant 3 <= m_CapacityMultiplier + m_CapacityIncrement;

  /**
   * Constructs an empty vector with initial
   * capacity zero.
   */
  public FastVector() {

    //m_Objects = new char [0][];
    m_Objects = new String[0];
  }

  /**
   * Constructs a vector with the given capacity.
   *
   * @param capacity the vector's initial capacity
   */
  //@ requires capacity >= 0;
  public  FastVector(int capacity) {

    //m_Objects = new char[capacity][];
    m_Objects = new String[capacity];
  }

  /**
   * Adds an element to this vector. Increases its
   * capacity if its not large enough.
   *
   * @param element the element to add
   */
  public final void addElement(String element) {//char[]

    //char[][] newObjects;
    String[] newObjects;

    if (m_Size == m_Objects.length) {
      newObjects = new String[m_CapacityMultiplier *         //char
                             (m_Objects.length +
                              m_CapacityIncrement)];//[]
                              
      System.arraycopy(m_Objects, 0, newObjects, 0, m_Size);
      
      m_Objects = newObjects;
    }
    m_Objects[m_Size] = element;
    m_Size++;
  }

  /**
   * Returns the capacity of the vector.
   *
   * @return the capacity of the vector
   */
  //@ ensures \result == m_Objects.length;
  public final /*@pure@*/ int capacity() {

    return m_Objects.length;
  }

  /**
   * Produces a shallow copy of this vector.
   *
   * @return the new vector
   */
 
  public final FastVector copy() {

    FastVector copy = new FastVector(m_Objects.length);

    copy.m_Size = m_Size;
    copy.m_CapacityIncrement = m_CapacityIncrement;
    copy.m_CapacityMultiplier = m_CapacityMultiplier;
    System.arraycopy(m_Objects, 0, copy.m_Objects, 0, m_Size);
    return  copy;
  }


//=====================================================================   

  /**
   * Clones the vector and shallow copies all its elements.
   * The elements have to implement the Copyable interface.
   *
   * @return the new vector
   */

 /**
  public FastVector copyElements() {

    FastVector copy = new FastVector(m_Objects.length);

    copy.m_Size = m_Size;
    copy.m_CapacityIncrement = m_CapacityIncrement;
    copy.m_CapacityMultiplier = m_CapacityMultiplier;
    for (int i = 0; i < m_Size; i++) {
      copy.m_Objects[i] = ((Copyable)m_Objects[i]).copy();
    }
    return copy;
  }
**/

//============================================================
  /**
   * Returns the element at the given position.
   *
   * @param index the element's index
   * @return the element with the given index
   */
  //@ requires 0 <= index;
  //@ requires index < m_Objects.length;
  public  /*@pure@*/ String elementAt(int index) { //char[]

    return m_Objects[index];
  }

  /**
   * Returns an enumeration of this vector.
   *
   * @return an enumeration of this vector
   */
  public final /*@pure@*/ Enumeration elements() {

    return new FastVectorEnumeration(this);
  }

  /**
   * Returns an enumeration of this vector, skipping the
   * element with the given index.
   *
   * @param index the element to skip
   * @return an enumeration of this vector
   */
  //@ requires 0 <= index && index < size();
  public final /*@pure@*/ Enumeration elements(int index) {

    return new FastVectorEnumeration(this, index);
  }

    /**
     * added by akibriya
     */
  public /*@pure@*/ boolean contains(String o) {//char[]
      if(o==null)
          return false;

      for(int i=0; i<m_Objects.length; i++){
      		boolean have = true;
      		if(m_Objects[i] != o){
              		have = false;
              		break;
      		}
      		/*
      		for(int j=0;j<m_Objects[i].length; j++){
          		if(m_Objects[i][j] != o[j]){
              		have = false;
              		break;
              	}
          }*/
          
          
          if(!have)
          		continue;
          else
          		return true;
      }

      return false;
  }


  /**
   * Returns the first element of the vector.
   *
   * @return the first element of the vector
   */
  //@ requires m_Size > 0;
  public final /*@pure@*/String firstElement() {//char[]

    return m_Objects[0];
  }

  /**
   * Searches for the first occurence of the given argument,
   * testing for equality using the equals method.
   *
   * @param element the element to be found
   * @return the index of the first occurrence of the argument
   * in this vector; returns -1 if the object is not found
   */
  public final /*@pure@*/ int indexOf(/*@non_null@*/String   element) {//char[]

      for(int i=0; i<m_Objects.length; i++){
      		boolean have = false;
      		
      		if(m_Objects[i] == element){
              		have = true;
              	} else {
              		have = false;
              		break;
              	}
      		
      		
      		/*
      		for(int j=0;j<m_Objects[i].length; j++){
          		have = false;
          		
          		if(m_Objects[i][j] == element[j]){
              			have = true;
              		} else {
              			have = false;
              			break;
              		}
          	}*/
          	
          if(!have)
          		continue;
          else
          		return i;
      }
      
    return -1;
  }

  /**
   * Inserts an element at the given position.
   *
   * @param element the element to be inserted
   * @param index the element's index
   */
  public final void insertElementAt(String element, int index) {//char[]

    //char[][] newObjects;
    String[] newObjects;

    if (m_Size < m_Objects.length) {
      System.arraycopy(m_Objects, index, m_Objects, index + 1,
                       m_Size - index);
      m_Objects[index] = element;
    } else {
    	
    	newObjects = new String[m_CapacityMultiplier *
                             (m_Objects.length +
                              m_CapacityIncrement)];
    	
      /*newObjects = new char[m_CapacityMultiplier *
                             (m_Objects.length +
                              m_CapacityIncrement)][];
      */
                            
      System.arraycopy(m_Objects, 0, newObjects, 0, index);
      newObjects[index] = element;
      System.arraycopy(m_Objects, index, newObjects, index + 1,
                       m_Size - index);
      m_Objects = newObjects;
    }
    m_Size++;
  }

  /**
   * Returns the last element of the vector.
   *
   * @return the last element of the vector
   */
  //@ requires m_Size > 0;
  public final /*@pure@*/String  lastElement() {//char[]

    return m_Objects[m_Size - 1];
  }

  /**
   * Deletes an element from this vector.
   *
   * @param index the index of the element to be deleted
   */
  //@ requires 0 <= index && index < m_Size;
  public final void removeElementAt(int index) {

    System.arraycopy(m_Objects, index + 1, m_Objects, index,
                     m_Size - index - 1);
    m_Size--;
  }

  /**
   * Removes all components from this vector and sets its
   * size to zero.
   */
  public final void removeAllElements() {

   // m_Objects = new char[m_Objects.length][];
    m_Objects = new String[m_Objects.length];
    m_Size = 0;
  }

  /**
   * Appends all elements of the supplied vector to this vector.
   *
   * @param toAppend the FastVector containing elements to append.
   */
  public final void appendElements(FastVector toAppend) {

    setCapacity(size() + toAppend.size());
    System.arraycopy(toAppend.m_Objects, 0, m_Objects, size(), toAppend.size());
    m_Size = m_Objects.length;
  }

  /**
   * Returns all the elements of this vector as an array
   *
   * @param an array containing all the elements of this vector
   */
  
  /*public final String [] toArray() {

    String [] newObjects = new String[size()];
    System.arraycopy(m_Objects, 0, newObjects, 0, size());
    return newObjects;
  }*/

  /**
   * Sets the vector's capacity to the given value.
   *
   * @param capacity the new capacity
   */
  public final void setCapacity(int capacity) {

    //char[][] newObjects = new char[capacity][];
    String[] newObjects = new String[capacity];

    System.arraycopy(m_Objects, 0, newObjects, 0, Math.min(capacity, m_Size));
    m_Objects = newObjects;
    if (m_Objects.length < m_Size)
      m_Size = m_Objects.length;
  }

  /**
   * Sets the element at the given index.
   *
   * @param element the element to be put into the vector
   * @param index the index at which the element is to be placed
   */
  //@ requires 0 <= index && index < size();
  public final void setElementAt(String element, int index) {//char[]

    m_Objects[index] = element;
  }

  /**
   * Returns the vector's current size.
   *
   * @return the vector's current size
   */
  //@ ensures \result == m_Size;
  public final /*@pure@*/ int size() {

    return m_Size;
  }

  /**
   * Swaps two elements in the vector.
   *
   * @param first index of the first element
   * @param second index of the second element
   */
  //@ requires 0 <= first && first < size();
  //@ requires 0 <= second && second < size();
  public final void swap(int first, int second) {

    String help = m_Objects[first];//char[]

    m_Objects[first] = m_Objects[second];
    m_Objects[second] = help;
  }

  /**
   * Sets the vector's capacity to its size.
   */
  public final void trimToSize() {

    //char[][] newObjects = new char[m_Size][];
    String[] newObjects = new String[m_Size];
    System.arraycopy(m_Objects, 0, newObjects, 0, m_Size);
    m_Objects = newObjects;
  }
}


