package com.acuo.persist.consistency;

import com.opengamma.strata.collect.result.Result;

import java.util.List;

public interface CheckManager {

    List<Result> check();
}
