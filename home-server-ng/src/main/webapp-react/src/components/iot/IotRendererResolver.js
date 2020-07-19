export function iotResolver() {
    const resolvers = {
        BUZZER: {
            icon: "record_voice_over",
            title: "Cliquer pour envoyer le buzz"
        },
        TRIGGER: {
            icon: "call_merge",
            title: "Cliquer pour controller le status"
        },
        SIREN: {
            icon: "volume_up",
            title: "Cliquer pour tester la sirène"
        }
    }

    const resolveRender = (type) => resolvers[type];



    return {
        resolveRender: resolveRender
    };
}

export function iotFilter() {
    const onType = (type) => oneComponent => oneComponent.componentDescriptor.componentType === type;
    const triggers = onType('TRIGGER')
    const triggerables = () => oneComponent => !triggers(oneComponent)

    return {
        onType: onType,
        triggers: triggers,
        triggerables: triggerables
    }
}
