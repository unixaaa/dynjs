package org.dynjs.runtime.linker.js.global;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.js.GlobalObjectFilter;
import org.dynjs.runtime.linker.js.ReferenceStrictnessFilter;
import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class GlobalPropertySetLink extends SmartLink implements Guard {

    public GlobalPropertySetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object context, String propertyName, Object value) {
        return (receiver instanceof Reference) && (((Reference) receiver).getBase() == Types.UNDEFINED) && !(((Reference) receiver).isStrictReference());
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(GlobalPropertySetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class, Object.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .permute(1, 1, 2, 3, 0)
                .filter(0, GlobalObjectFilter.INSTANCE)
                .filter(4, ReferenceStrictnessFilter.INSTANCE)
                .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                .invoke(lookup().findVirtual(JSObject.class, "put", methodType(void.class, ExecutionContext.class, String.class, Object.class, boolean.class)))
                .target();
    }

}
