package utils

import config.KagConfig

/**
 * Ensures that a specified condition is true. If the condition evaluates to false
 * and precondition checks are enabled, an [IllegalArgumentException] is thrown with
 * the provided message.
 *
 * @param message The message to include in the exception if the condition fails.
 *                Defaults to "Precondition failed".
 * @param condition A lambda function that returns a Boolean value representing the
 *                  condition to check. The function is only executed if precondition
 *                  checks are enabled.
 *
 * @throws IllegalArgumentException if [KagConfig.PRECONDITIONS_CHECKS_ENABLED] is true
 * and the [condition] evaluates to false.
 */
fun ensure(
    message: String = "Precondition failed",
    condition: () -> Boolean
) {
    if (KagConfig.PRECONDITIONS_CHECKS_ENABLED && !condition()) {
        throw IllegalArgumentException(message)
    }
}