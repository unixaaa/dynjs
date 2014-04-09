package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.runtime.ExecutionContext;

public class StringLiteral extends Operand {
    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return this.value;
    }
}