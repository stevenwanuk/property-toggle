package com.sven.toggle.condition;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnToggleCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnToggle.class.getName());
        PropertyToggle[] enabledToggles = (PropertyToggle[]) attributes.get("whenToggledOn");
        PropertyToggle[] disableToggles = (PropertyToggle[]) attributes.get("whenToggledOff");

        return getMatchOutcome(context, metadata, enabledToggles, disableToggles);
    }

    private ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata, PropertyToggle[] enabledToggles, PropertyToggle[] disableToggles) {


        List<ConditionMessage> conditionMessageList = new ArrayList<>();

        List<ToggleCondition> toggleConditions = new ArrayList<>();
        toggleConditions.addAll(Arrays.stream(enabledToggles).map(s -> new ToggleCondition(s, true)).collect(Collectors.toList()));
        toggleConditions.addAll(Arrays.stream(disableToggles).map(s -> new ToggleCondition(s, false)).collect(Collectors.toList()));

        for (ToggleCondition toggleCondition : toggleConditions) {

            if (toggleCondition.matches(context, metadata)) {


                conditionMessageList.add(buildNotMatchMessage(toggleCondition));

            } else {

                ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnToggle.class);
                String reason = MessageFormat.format("expected Toggle: {0}, but actual status: {1}", toggleCondition, !toggleCondition.toggledOn);
                return ConditionOutcome.noMatch(message.because(reason));
            }
        }

        return ConditionOutcome.match(ConditionMessage.of(conditionMessageList));
    }

    private ConditionMessage buildNotMatchMessage(ToggleCondition toggleCondition) {

        PropertyToggle toggle = toggleCondition.toggle;
        ConditionMessage.Builder message = ConditionMessage.forCondition(toggleCondition.toString());
        String reason = MessageFormat.format("ToggleCondition: {0}, matched", toggleCondition);
        return message.because(reason);
    }

    class ToggleCondition implements Condition {

        private PropertyToggle toggle;
        private boolean toggledOn;

        public ToggleCondition(PropertyToggle toggle, boolean toggledOn) {

            this.toggle = toggle;
            this.toggledOn = toggledOn;
        }


        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

            String actualValue = context.getEnvironment().getProperty(toggle.getPropertyName());

            return toggle.getPropertyValue().equalsIgnoreCase(actualValue) == toggledOn;
        }

        @Override
        public String toString() {
            return "ToggleCondition{" +
                    "toggle=" + toggle +
                    ", toggledOn=" + toggledOn +
                    '}';
        }
    }

}
