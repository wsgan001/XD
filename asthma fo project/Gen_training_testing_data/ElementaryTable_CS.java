/********************************************************
*	ElementaryTable_CS 類別 (因素項目表)		*
*							*
*	由HashMap修改而來				*
*	其每個元素內容定義在Element_CS			*
*							*
*	作者：Guo-Cheng	    最後修改日:2007/06/30	*
********************************************************/


package Gen_training_testing_data;



import  java.util.Iterator;
import  java.util.Set;
import  java.util.AbstractSet;
import  java.io.*;

public class ElementaryTable_CS {
	static final int MAXIMUM_CAPACITY = 1 << 30;
	transient Element_CS[] table;
	transient int size;
	int threshold = 128;        //CAPACITY
	float loadFactor = 0.95f; 
	
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
    
    	public boolean ContainsKey(char ch) {
		char[] name=new char[1];
		name[0]=ch;
		return getElement(name) != null;
 	}  
 	 
     	public boolean ContainsKey(char ch, char ch2) {
		char[] name=new char[2];
		name[0]=ch;
		name[1]=ch2;
		return getElement(name) != null;
 	} 	
 	
 	
	public ElementaryTable_CS(int initialCapacity, float loadFactor) {
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
        	table = new Element_CS[capacity];
    	}
  
    	public ElementaryTable_CS(int initialCapacity) {
        	this(initialCapacity, 0.95f);
    	}

    	public ElementaryTable_CS() {
        	table = new Element_CS[threshold];
    	}

    	static int hash(int h){ 
        	h += ~(h << 9);
        	h ^=  (h >>> 14);
        	h +=  (h << 4);
        	h ^=  (h >>> 10);
        	return h;
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

    	public String getInfo(char[] key) {
        	char[] k = key; 
        	int hash = hash(hashCode(k));
        	int i = indexFor(hash, table.length);
        	Element_CS e = table[i]; 
        	while (true) {
            		if (e == null)
                		return null;
            		if (e.equals(k)) 
                		return e.value;
            		e = e.next;
        	}
    	}

    	public String getInfo(String key) {
        	char[] k =key.toCharArray(); 
        	int hash = hash(hashCode(k));
        	int i = indexFor(hash, table.length);
        	Element_CS e = table[i]; 
        	while (true) {
            		if (e == null)
                		return null;
            		if (e.equals(k)) 
                		return e.value;
            		e = e.next;
        	}
    	}
    	public boolean containsKey(char[] key) {
        	char[] k = key; 
        	int hash = hash(hashCode(k));
        	int i = indexFor(hash, table.length);
        	Element_CS e = table[i]; 
        	while (e != null) {
            		if (e.equals(k)) 
                		return true;
            		e = e.next;
        	}
        	return false;
    	}

    	Element_CS getElement(char[] key) {
        	char[] k = key;
        	int hash = hash(hashCode(k));
        	int i = indexFor(hash, table.length);
        	Element_CS e = table[i]; 
        	while (e != null && !(e.equals(k)))
            		e = e.next;
        	return e;
    	}

	public void add(String str, String value) {
    		char[] k = str.toCharArray();
    		this.add(k, value);
    	}
    	
    	public void add(char ch, String value) {
    		char[] k = new char[1];
    		k[0]=ch;
    		this.add(k, value);
    	}

    	public void add(char ch,char ch2,String value) {
    		char[] k = new char[2];
    		k[0]=ch;
    		k[1]=ch2;
    		this.add(k, value);
    	}    	
    	public void add(char[] key, String value) {
        	char[] k = key; 
        	int hash = hash(hashCode(k));
        	int i = indexFor(hash, table.length);

        	for (Element_CS e = table[i]; e != null; e = e.next) 
            		if (e.equals(k)) {
            			e.value += value;
                		//e.value += value;
                		return; 
            		}
        	addElement(hash, k, value, i);
        	return;
    	}
    	
    	void resize(int newCapacity) {
        	Element_CS[] oldTable = table;
        	int oldCapacity = oldTable.length;
        	if (size < threshold || oldCapacity > newCapacity) 
            		return;
        	Element_CS[] newTable = new Element_CS[newCapacity];
        	transfer(newTable);
        	table = newTable;
        	threshold = (int)(newCapacity * loadFactor);
	}
	
    	void transfer(Element_CS[] newTable) {
        	Element_CS[] src = table;
        	int newCapacity = newTable.length;
        	for (int j = 0; j < src.length; j++) {
            		Element_CS e = src[j];
            		if (e != null) {
                		src[j] = null;
                		do {
                    			Element_CS next = e.next;
                    			int i = indexFor(e.hash, newCapacity);  
                    			e.next = newTable[i];
                    			newTable[i] = e;
                    			e = next;
                		} while (e != null);
            		}
        	}
    	}
    	
    	void addElement(int hash, char[] key, String value, int bucketIndex) {
        	table[bucketIndex] = new Element_CS(hash, key, value, table[bucketIndex]);
        	if (size++ >= threshold) 
            		resize(2 * table.length);
    	}
    	
    	void createElement(int hash, char[] key, String value, int bucketIndex) {
        	table[bucketIndex] = new Element_CS(hash, key, value, table[bucketIndex]);
        	size++;
    	}

    	private abstract class HashIterator implements Iterator {
        	Element_CS next;  
        	int index;       
        	Element_CS current;     

        	HashIterator() {
            		Element_CS[] t = table;
            		int i = t.length;
            		Element_CS n = null;
            		if (size != 0) { // advance to first entry
                		while (i > 0 && (n = t[--i]) == null)
                    		;
            		}
           		next = n;
            		index = i;
        	}

        	public boolean hasNext() {
            		return next != null;
        	}

        	Element_CS nextElement() { 
            		Element_CS e = next;
            		Element_CS n = e.next;
            		Element_CS[] t = table;
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
        	public boolean contains(Element_CS o) {
            		Element_CS e = o; 
            		Element_CS candidate = getElement(e.getKey());
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
