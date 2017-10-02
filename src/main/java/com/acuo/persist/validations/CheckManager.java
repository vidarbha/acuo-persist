package com.acuo.persist.validations;

import com.opengamma.strata.collect.result.Result;

import java.util.List;

public interface CheckManager {

    List<Result> check();
}
