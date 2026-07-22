package io.senftresearch.openrd.encoding.commands;


public abstract class AbstractRDCommand implements RDCommand {



    public static abstract class AbstractRDCommandBuilder<T extends AbstractRDCommandBuilder<T>>{
        protected Object[] args;
        protected abstract AbstractRDCommand create();
        protected abstract boolean requiredArgsInitialized();
        public RDCommand build(){
            AbstractRDCommand command = create();

            if(!requiredArgsInitialized()){
                throw new IllegalArgumentException("Not all args are initialised for this command!");
            }

            return command;
        }
    }

}


