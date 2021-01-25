
import UserDefinedDS.DynamicArray;

/*
HashMap :

    constructor:
        HashMap()
        HashMap(hashMap)

    member functions:
        void put(key,value) --> overwrite the key- value pair if it already exsist.
        boolean putIfAbsent(key,value) --> put only if the pair is absent 
                                          return value show whether the pair is present or not
        void removeKey(key)
        Pair remove(key,value) - remove and return the specific pair.
        value get(key) 
        key[] getKeys()
        values[] getValues()
        String toString()
        boolean isEmpty()
        boolean isAkey(key) --> check whether the given key is present or not

*/

class HashMapMain{
    public static void main(String[] args){
        HashMap<Integer,String> obj = new HashMap<Integer,String>();
        
        Integer[] a = { 1, 2, 3, 4, 5, 6 };
        String[]  b = { "s", "r", "i", "r", "a", "m" };
        for( int i = 0 ; i < a.length ; i++ ){
            obj.put( a[i], b[i] );
        }
        System.out.println( obj.toString() );
        System.out.println( obj.get(3) );
        System.out.println( obj.isAKey( 33 ) );
        System.out.println( obj.toString() );
        System.out.println( obj.size() );
        System.out.println( obj.removePair(3, "i") );
        System.out.println( obj.removePair(4, "r") );
        System.out.println( obj.toString() );
        System.out.println( obj.size() );
        System.out.println( obj.getKeys().toString() );
        System.out.println( obj.getValues().toString() );

        HashMap<Integer,String> obj1 = new HashMap<Integer,String>( obj );
        System.out.println( obj1.size() );
        System.out.println( obj1.toString() );
    }
}

// Pair data structure
public class Pair<T1, T2>{ 
    public int hashValue;
    private T1 key;
    private T2 value;
    public Pair<T1, T2> nextPair = null;

    public Pair(T1 key, T2 value){
        this.hashValue = key.hashCode();
        this.key   =  key;
        this.value =  value;
    }

    public Pair(Pair<T1, T2> pair){
        this.hashValue = pair.hashValue;
        this.key       = pair.getKey();
        this.value     = pair.getValue();
        if( pair.nextPair != null ){
            
            this.nextPair  = new Pair(pair.nextPair);
        }
    }

    public T1 getKey() { return this.key; }
    public T2 getValue(){ return this.value; }

    public void setKey(T1 key){ this.key = key; } 
    public void setValue(T2 value){ this.value = value; }

    public String toString(){
        return "( " + this.key + " : " + this.value  + " )";
    }
}

//hash map data structure
class HashMap<T1, T2>{

    private Object[] array;
    private int elementPresent = 0;

    public HashMap(){
        this.array = new Object[16];
    }

    public HashMap(HashMap<T1, T2> container){
        this.array = new Object[16];
        T1[] keys   = container.getKeys().toArray();
        T2[] values = container.getValues().toArray();
        for( int i = 0 ; i < keys.length ; i++ ){
            this.add( keys[i], values[i] );
        }
    }

    public int size(){ return elementPresent; }
    public boolean isEmpty(){return elementPresent == 0;}

    private int getIndex(T1 key){
        return key.hashCode() % this.array.length;
    }

    public boolean isAKey(T1 key){
        return this.isKeyPresent(key).getKey();
    }

    private Pair< Boolean, Pair<T1,T2> > isKeyPresent(T1 key){
        int index = this.getIndex(key);
        if(this.array[index] == null){ return new Pair(false, null); }
        else{
            Pair<T1, T2> temp = (Pair<T1, T2>)this.array[index];
            while(temp != null){
                if(temp.hashValue == key.hashCode()) { return new Pair(true, temp); }
                temp = temp.nextPair;
            }
        }
        return new Pair(false, null);
    }

    private void add(T1 key, T2 value){
        Pair<T1, T2> pair = new Pair(key,value);

        int index = this.getIndex(key);
        if(this.array[index] == null){ this.array[index] = pair; }
        else{
            Pair<T1, T2> temp = (Pair<T1, T2>)this.array[index];
            while(temp.nextPair != null){
                if(temp.hashValue == key.hashCode()) { this.array[index] = pair; }
                temp = temp.nextPair;
            }
            temp.nextPair = pair;
        }
        this.elementPresent++;
    }

    public void put(T1 key, T2 value){
        Pair< Boolean, Pair<T1,T2> > status = this.isKeyPresent(key);
        if(status.getKey()){
            status.getValue().setValue(value);;
        }
        else{
            this.add( key, value );
        }
        return ;
    }
    
    public boolean putIfAbsent(T1 key, T2 value){
        Pair< Boolean, Pair<T1,T2> > status = this.isKeyPresent(key);
        if( ! status.getKey() ){ this.add( key, value ); }
        return status.getKey();
    }

    public void removeKey(T1 key){
        int index = this.getIndex(key);
        Pair<T1, T2> element = (Pair<T1, T2>)this.array[index];

        if(element.nextPair == null){ element.setKey(null); }
        else{
            Pair<T1, T2> temp = element;
            while(temp != null){
                if(temp.hashValue == key.hashCode()) { temp.setKey(null); }
                temp = temp.nextPair;
            }
        }
        return  ;
    }

    public Pair<T1, T2> removePair(T1 key, T2 value){
        int index = this.getIndex(key);

        Pair<T1, T2> element = (Pair<T1, T2>)this.array[index], returnPair = null;

        if(element.hashValue == key.hashCode()){
            returnPair = element ;
            this.array[index] = element.nextPair;
        }

        else{
            Pair<T1, T2> temp = element,previous = element;
           
            while(temp != null){
                if( temp.hashValue == key.hashCode()) { 
                    returnPair = temp ;
                    previous = temp.nextPair; 
                    break;
                }
                previous = temp;
                temp = temp.nextPair;
            }
        }
        this.elementPresent--;
        return returnPair;
    }

    public T2 get(T1 key){
        int index = this.getIndex(key);
        Pair<T1, T2> element = (Pair<T1, T2>)this.array[index];

        if(element.nextPair == null){ return element.getValue(); }
        else{
            Pair<T1, T2> temp = element;
            while(temp != null){
                if(temp.hashValue == key.hashCode()) { return temp.getValue(); }
                temp = temp.nextPair;
            }
        }
        return null;
    }

    public DynamicArray<T1> getKeys(){
        DynamicArray<T1> keys = new DynamicArray<T1>();
        
        Pair<T1, T2> element;
        for( int i = 0 ; i < this.array.length ; i++ ){

            element = (Pair<T1, T2>)this.array[i];
            if( element != null){

                if( element.nextPair == null ){keys.add(element.getKey());}
                else{
                    Pair<T1, T2> temp = element;
                    while(temp != null){
                        keys.add(temp.getKey());
                        temp = temp.nextPair;
                    }
                }

            }

        }
        return keys;

    }

    public DynamicArray<T2> getValues(){
        DynamicArray<T2> values = new DynamicArray<T2>();
        
        Pair<T1, T2> element;
        for( int i = 0 ; i < this.array.length ; i++ ){

            element = (Pair<T1, T2>)this.array[i];
            if( element != null){

                if( element.nextPair == null ){values.add(element.getValue());}
                else{
                    Pair<T1, T2> temp = element;
                    while(temp != null){
                        values.add(temp.getValue());
                        temp = temp.nextPair;
                    }
                }

            }
            
        }
        return values; 
    }

    public String toString(){
        StringBuilder string = new StringBuilder("{ ");
        Pair<T1, T2> element;

        for(int i = 0; i < this.array.length ; i++ ){
            element = (Pair<T1, T2>)this.array[i];

            if(element != null){

                if(element.nextPair == null){
                    string.append( this.array[i].toString() );
                }
                else{
                    Pair<T1, T2> temp = element;
                    while(temp != null){
                        string.append( temp.toString() );
                        temp = temp.nextPair;
                        if(temp != null) { string.append( ", " ); }
                    }
                }

                if(i != this.array.length - 1){
                    if(this.array[i+1] != null){
                    string.append( ", " );
                    }
                }

            }
            
        }
        string.append(" }");
        return new String(string);
    }

}
