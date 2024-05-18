package com.georgen.letterwind.broker.conveyor;

import com.georgen.letterwind.broker.conveyor.lowlevel.ErrorHandlingConveyor;
import com.georgen.letterwind.broker.conveyor.highlevel.ReceptionConveyor;
import com.georgen.letterwind.broker.conveyor.highlevel.DispatchConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class ConveyorFactory {

    public static <T> MessageConveyor<T> createConveyor(Envelope<T> envelope, FlowEvent event){
        switch (event){
            case DISPATCH: {
                return new DispatchConveyor<T>();
            }
            case RECEPTION: {
                return new ReceptionConveyor<T>();
            }
            default: {
                return new ErrorHandlingConveyor<T>();
            }
        }
    }
}
