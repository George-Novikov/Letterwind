package com.georgen.letterwind.broker.conveyor;

import com.georgen.letterwind.broker.conveyor.lowlevel.ErrorHandlingConveyor;
import com.georgen.letterwind.broker.conveyor.highlevel.ReceivingConveyor;
import com.georgen.letterwind.broker.conveyor.highlevel.SendingConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class ConveyorFactory {

    public static <T> MessageConveyor<T> createConveyor(Envelope<T> envelope, FlowEvent event){
        switch (event){
            case DISPATCH: {
                return new SendingConveyor<T>();
            }
            case RECEPTION: {
                return new ReceivingConveyor<T>();
            }
            default: {
                return new ErrorHandlingConveyor<T>();
            }
        }
    }
}
