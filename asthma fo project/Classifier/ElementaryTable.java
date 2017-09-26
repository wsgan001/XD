/********************************************************
*	ElementaryTable 摸 (兜ヘ)		*
*							*
*	パHashMapэτㄓ				*
*	ㄤ–じず甧﹚竡Element			*
*	ず场矪瞶じ, char[]ㄓ暗			*
*	add()よ猭ㄓе仓璸や笆		*
*							*
*	Bear	程эら:94/5/4		*
********************************************************/

//package IntegratedTwoArffs;
package Classifier;

import  java.util.Iterator;
import  java.util.Set;
import  java.util.AbstractSet;
import  java.io.*;

public class ElementaryTable {
	static final int MAXIMUM_CAPACITY = 1 << 30;	//程じ计秖(2^30)
	transient Element[] table;	//じ皚
	transient int size;		//じ计秖
	int threshold = 128;		//﹍じ
	float loadFactor = 0.95f; 	//璽更
    	transient volatile int modCount;
    
	public boolean NoContainsKey(String key) {
		char[]k = key.toCharArray();
        	return getElement(k) == null;
	}    
    	
	public boolean NoContainsKey(char[] key) {
        	return getElement(key) == null;
	}
    
    	public boolean NoContainsKey(char ch) {
		char[] name=new char[1];
		name[0]=ch;
		return getElement(name) == null;
 	}   
 	
 	public boolean ContainsKey(char[] key) {
        	return getElement(key) != null;
	}

 	public boolean ContainsKey(String key) {
 		char[]k = key.toCharArray();
        	return getElement(k) != null;
	}

    
    	public boolean ContainsKey(char ch) {
		char[] name=new char[1];
		name[0]=ch;
		return getElement(name) != null;
 	}   
 	
	public ElementaryTable(int initialCapacity, float loadFactor) {
        	if (initialCapacity < 0)
            		throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        	if (initialCapacity > MAXIMUM_CAPACITY)
            		initialCapacity = MAXIMUM_CAPACITY;
        	if (loadFactor <= 0 || Float.isNaN(loadFactor))
            		throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
       		int capacity = 1;
        	while (capacity < initialCapacity) 
            		capacity <<= 1;
        	this.loadFactor = loadFactor;
        	threshold = (int)(capacity * loadFactor);
        	table = new Element[capacity];
    	}
  
    	public ElementaryTable(int initialCapacity) {
        	this(initialCapacity, 0.95f);
    	}

    	public ElementaryTable() {
        	table = new Element[threshold];
    	}

	public int hashCode(char[] key) {
		int h = 0;
       	    	for (int i = 0; i < key.length; i++) 
                	h = 31*h + key[i];
        	return h;
    	}

    	static int indexFor(int h, int length) {
        	return h & (length-1);
    	}
    	
    	public int size() {
        	return size;
    	}

    	public int getValue(char[] key) {
        	char[] k = key; 
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);
        	Element e = table[i]; 
        	while (true) {
            		if (e == null)
                		return 0;
            		if (e.equals(k)) 
                		return e.value;
            		e = e.next;
        	}
    	}

    	  	
    	public int getValue(char key) {
        	char[] k = new char[1];
        	k[0]= key; 
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);
        	Element e = table[i]; 
        	while (true) {
            		if (e == null)
                		return 0;
            		if (e.equals(k)) 
                		return e.value;
            		e = e.next;
        	}
    	}    
    	
    	public int getValue(String key) {
        	
        	char[] k = key.toCharArray();        	
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);
        	Element e = table[i]; 
        	while (true) {
            		if (e == null)
                		return 0;
            		if (e.equals(k)) 
                		return e.value;
            		e = e.next;
        	}
    	} 
    		

    	public boolean containsKey(char[] key) {
        	char[] k = key; 
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);
        	Element e = table[i]; 
        	while (e != null) {
            		if (e.equals(k)) 
                		return true;
            		e = e.next;
        	}
        	return false;
    	}

    	Element getElement(char[] key) {
        	char[] k = key;
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);
        	Element e = table[i]; 
        	while (e != null && !(e.equals(k)))
            		e = e.next;
        	return e;
    	}

	public void add(String str, int value) {
    		char[] k = str.toCharArray();
    		this.add(k, value);
    	}

    	
    	public void add(char ch, int value) {
    		char[] k = new char[1];
    		k[0]=ch;
    		this.add(k, value);
    	}

    	public void add(char ch1,char ch2, int value) {
    		char[] k = new char[2];
    		k[0]=ch1;
    		k[1]=ch2;
    		
    		this.add(k, value);
    	}

    	
    	public void add(char[] key, int value) {
        	char[] k = key; 
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);

        	for (Element e = table[i]; e != null; e = e.next) 
            		if (e.equals(k)) {
                		e.value += value;
                		return; 
            		}
		modCount++;            		
        	addElement(hash, k, value, i);
        	return;
    	}
    	
    	void resize(int newCapacity) {
        	Element[] oldTable = table;
        	int oldCapacity = oldTable.length;
        	if (size < threshold || oldCapacity > newCapacity) 
            		return;
        	Element[] newTable = new Element[newCapacity];
        	transfer(newTable);
        	table = newTable;
        	threshold = (int)(newCapacity * loadFactor);
	}
	
    	void transfer(Element[] newTable) {
        	Element[] src = table;
        	int newCapacity = newTable.length;
        	for (int j = 0; j < src.length; j++) {
            		Element e = src[j];
            		if (e != null) {
                		src[j] = null;
                		do {
                    			Element next = e.next;
                    			int i = indexFor(e.hash, newCapacity);  
                    			e.next = newTable[i];
                    			newTable[i] = e;
                    			e = next;
                		} while (e != null);
            		}
        	}
    	}
    	
    	//======== 穝糤 ========
        public void remove(char[] key1) {
        	Element e = removeEntryForKey(key1);        	        
        	//return (e == null ? e : e.value);
    	}
    	
    	Element removeEntryForKey(char[] key1) {
        	//char[] k = maskNull(key);
        	char[] k =key1;
        	int hash = hashCode(k);
        	int i = indexFor(hash, table.length);
        	Element prev = table[i];//Entry
        	Element e = prev;//Entry

        	while (e != null) {
        	   Element next = e.next; //Entry
            	if (e.hash == hash && eq(k, e.key)) {
                	//modCount++;
                	size--;
                	if (prev == e) 
                    		table[i] = next;
                	else
                    		prev.next = next;
                	//e.recordRemoval(this);
               return e;
        }
            prev = e;
            e = next;
        }
   
        return e;
    }    	
  
    static boolean eq(Object x, Object y) {
        return x == y || x.equals(y);
    }
  
  
  /*
     Element removeMapping() {//Object o
        /*
        if (!(o instanceof Map.Entry))
            return null;
	*/
/*	
        //Map.Entry entry = (Map.Entry)o;
        Element entry = (Element)o;
        
        //char[] k = maskNull(entry.getKey());
        //char[] k = maskNull(entry.getKey());
        int hash = hash(k);
        int i = indexFor(hash, table.length);
        Element prev = table[i];
        Element e = prev;

        while (e != null) {
            Element next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                //modCount++;
                size--;
                if (prev == e) 
                    table[i] = next;
                else
                    prev.next = next;
                //e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }
   
        return e;
    } 
  */
    	
    	//================  
  
  
  
  
  
    	
    	void addElement(int hash, char[] key, int value, int bucketIndex) {
        	table[bucketIndex] = new Element(hash, key, value, table[bucketIndex]);
        	if (size++ >= threshold) 
            		resize(2 * table.length);
    	}
    	
    	void createElement(int hash, char[] key, int value, int bucketIndex) {
        	table[bucketIndex] = new Element(hash, key, value, table[bucketIndex]);
        	size++;
    	}

    	private abstract class HashIterator implements Iterator {
        	Element next;  
        	int index;       
        	Element current;     

        	HashIterator() {
            		Element[] t = table;
            		int i = t.length;
            		Element n = null;
            		if (size != 0) { // advance to first entry
                		while (i > 0 && (n = t[--i]) == null) ;
            		}
           		next = n;
            		index = i;
        	}

        	public boolean hasNext() {
            		return next != null;
        	}

        	Element nextElement() { 
            		Element e = next;
            		Element n = e.next;
            		Element[] t = table;
            		int i = index;
            		while (n == null && i > 0)
                		n = t[--i];
            		index = i;
            		next = n;
            		return current = e;
        	}

        	public void remove() {}

    	}

    	private class ElementIterator extends HashIterator {
        	public Object next() {
            		return (Object) nextElement();
        	}
    	}
    	
    	Iterator newElementIterator()   {
        	return new ElementIterator();
    	}
    	
    	private transient Set ElementSet = null;
    	
    	public Set ElementSet() {
        	Set es = ElementSet;
        	return (es != null ? es : (ElementSet = new ElementSet()));
    	}

    	private class ElementSet extends AbstractSet {
        	public Iterator iterator() {
            		return newElementIterator();
        	}
        	public boolean contains(Element o) {
            		Element e = o; 
            		Element candidate = getElement(e.getKey());
           		return candidate != null && candidate.equals(e);
        	}
        	public boolean remove(Object o) {
            		return true; 
        	}
        	public int size() {
            		return size;
        	}
        	public void clear() {}
    	}
    	int   capacity()     { return table.length; }
    	float loadFactor()   { return loadFactor;   }
}
