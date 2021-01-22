class DynamicArray<Type>{
    
    private Object[] array = new Object[0];
    private int defaultCapacity = 0;

    
    public DynamicArray(){
        this.array = new Object[defaultCapacity];
    }

    public DynamicArray(int capacity){

        if(capacity < 0){
            throw new IllegalArgumentException(" Negative values are not allowed");
        }

        this.defaultCapacity = capacity;
        this.array = new Object[defaultCapacity];
    }

    // copy constructor
    public DynamicArray(DynamicArray<?extends Type> obj){
        this.addAll(obj.toArray());
    }

    public int size(){
        return this.array.length;
    }

    private boolean checkIndex(int index){
        return (index > 0) && ( index < this.size() -1 ) ? true : false ;
    }

    private void raiseException(int index){
        throw new IllegalArgumentException(String.format("index %d is out of range ",index));
    }

    public int indexOf(Type  element){
        for( int i = 0 ; i < this.array.length ; i++){
            if( this.array[i] == element ) return i;
        }
        return -1;
    }

    public Object elementAt(int index){
        if( !this.checkIndex(index) ){ this.raiseException(index); }
        return this.array[index];
    }

    // create new array and copy the elements of old array to new array
    public Object[] resize(int newSize){
        Object[] newArray = new Object[newSize];
        for( int i = 0 ; i < this.array.length ; i++ ){
            newArray[i] = this.array[i];
        }
        return newArray;
    }

    public void add(Type arg){
        this.array = this.resize(this.array.length + 1);
        this.array[this.array.length - 1] = arg;
    }

    public void addAll(Type[] container){
        int startIndex = this.array.length;
        this.array = this.resize( this.array.length + container.length );
        for( int i = 0 ; i < container.length ; i++ ){
            this.array[startIndex++] = container[i];
        }
    }
    
    public void remove(int index){
        if( !this.checkIndex(index) ) { this.raiseException(index); }
        removeSubArray(index, index);
    }

    //remove the elements from start to end index
    public void removeSubArray(int start , int end ){

        if( !this.checkIndex(start) ) { this.raiseException(start); }
        if( !this.checkIndex(end) ) { this.raiseException(end); }

        int intervalLength = end - start;
        for(int i = start ; i < this.array.length - intervalLength  - 1 ; i++){
            this.array[i] = this.array[i+1+intervalLength];
        }
        Object newArray[] = new Object[ this.array.length - intervalLength - 1 ];
        for(int i = 0 ; i < this.array.length - intervalLength - 1; i++){
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    public int delete(Type  element){
        if(this.size() == 0){
            return -1;
        }
        int index = this.indexOf(element);
        if(index != -1) remove(index);
        return index;
    }

    public void removeAll(Type [] container){
        for( Type  element : container ){
            this.delete(element);
        }
    }

    public void clear(){
        if(this.size() == 0){
            return ;
        }
        this.array = new Object[0];
    }

    public String toString(){
        StringBuilder stringRepresentation = new StringBuilder("[ ");
        for( int i = 0 ; i < this.array.length; i++ ){
            stringRepresentation.append( this.array[i] );
            if(i != this.array.length - 1 ){
                stringRepresentation.append( ", " );
            }
        }
        stringRepresentation.append( " ]" );
        return new String(stringRepresentation);
    }

    public Type[] toArray(){
        return (Type[])this.array;
    }

    public DynamicArray< Type > clone(){
        return new DynamicArray< Type >(this);
    }

    public boolean contain(Type element){
        for( int i = 0 ; i < this.array.length ; i++){
            if( this.array[i] == element ){
                return true;
            }
        }
        return false;
    }

    public boolean[] containAll(Type[] container){
        boolean[] resultArray = new boolean[container.length];
        int i = 0;
        for(Type arg : container){
            resultArray[i++] = this.contain(arg);
        }
        return resultArray;
    }

}