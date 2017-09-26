/********************************************************
*	Element ���O					*
*							*
*	�w�q�]�����ت��C�Ӥ��������e			*
*	�ݭn�P�]�����ت�(ElementaryTable)�f�t		*
*	�������e�� key(char[]), �P value(int)		*
*							*
*	�@�̡GBear	�̫�ק��:94/4/4		*
********************************************************/

package Gen_training_testing_data;

class Element {
	final char[] key;	//������key��
	int value;		//������value��
	int hash;		//������hash��
	Element next;		//�ۦPhash�Ȫ��U�@�Ӥ���
	
	Element(int h, char[] k, int v, Element n) { 
    		hash = h;
        	key = k;
        	value = v;
        	next = n;
	}

	public char[] getKey() { 
        	return key; 
	}

	public int getValue() {
        	return value;
	}

	public boolean equals(Element e) {  //�ˬdkey�ȬO�_�ۦP
		char[] ch = e.getKey();
		return equals(ch);
	}
	
	public boolean equals(char[] ch) {
		if (this.key.length!=ch.length) return false;
		for(int i=0; i<key.length; i++)
			if (key[i]!=ch[i]) return false;
		return true;
	}
	/*
	public recordRemoval(HashMap m) {
		
		
        }*/
}