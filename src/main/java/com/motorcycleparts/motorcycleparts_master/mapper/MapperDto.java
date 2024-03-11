package com.motorcycleparts.motorcycleparts_master.mapper;

public interface MapperDto <A, B>{
        B mapTo(A a);
        A mapFrom(B b);
}
