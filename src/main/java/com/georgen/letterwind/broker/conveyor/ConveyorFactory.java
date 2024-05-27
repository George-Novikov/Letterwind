package com.georgen.letterwind.broker.conveyor;

import com.georgen.letterwind.broker.conveyor.lowlevel.ErrorHandlingConveyor;
import com.georgen.letterwind.broker.conveyor.highlevel.ReceptionConveyor;
import com.georgen.letterwind.broker.conveyor.highlevel.DispatchConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.SuccessHandlingConveyor;
import com.georgen.letterwind.model.constants.FlowEvent;

public class ConveyorFactory {

    public static <T> MessageConveyor<T> createConveyor(FlowEvent event){
        switch (event){
            case DISPATCH: {
                return new DispatchConveyor<T>();
            }
            case RECEPTION: {
                return new ReceptionConveyor<T>();
            }
            case SUCCESS: {
                return new SuccessHandlingConveyor<>();
            }
            case ERROR: {
                return new ErrorHandlingConveyor<>();
            }
            default: {
                return new ErrorHandlingConveyor<T>();
            }
        }
    }
}
