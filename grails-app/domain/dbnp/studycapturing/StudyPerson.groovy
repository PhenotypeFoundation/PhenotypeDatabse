package dbnp.studycapturing

/**
 * 888       888 888    888 8888888888 8888888b.  8888888888
 * 888   o   888 888    888 888        888   Y88b 888
 * 888  d8b  888 888    888 888        888    888 888
 * 888 d888b 888 8888888888 8888888    888   d88P 8888888
 * 888d88888b888 888    888 888        8888888P"  888
 * 88888P Y88888 888    888 888        888 T88b   888
 * 8888P   Y8888 888    888 888        888  T88b  888
 * 888P     Y888 888    888 8888888888 888   T88b 8888888888
 *
 * 8888888 .d8888b.     88888888888 888    888 8888888888
 *   888  d88P  Y88b        888     888    888 888
 *   888  Y88b.             888     888    888 888
 *   888   "Y888b.          888     8888888888 8888888
 *   888      "Y88b.        888     888    888 888
 *   888        "888        888     888    888 888
 *   888  Y88b  d88P        888     888    888 888
 * 8888888 "Y8888P"         888     888    888 8888888888
 *
 *   888888        d8888 888     888     d8888 8888888b.   .d88888b.   .d8888b.
 *     "88b       d88888 888     888    d88888 888  "Y88b d88P" "Y88b d88P  Y88b
 *      888      d88P888 888     888   d88P888 888    888 888     888 888    888
 *      888     d88P 888 Y88b   d88P  d88P 888 888    888 888     888 888
 *      888    d88P  888  Y88b d88P  d88P  888 888    888 888     888 888
 *      888   d88P   888   Y88o88P  d88P   888 888    888 888     888 888    888
 *      88P  d8888888888    Y888P  d8888888888 888  .d88P Y88b. .d88P Y88b  d88P
 *      888 d88P     888     Y8P  d88P     888 8888888P"   "Y88888P"   "Y8888P"
 *    .d88P
 *  .d88P"
 * 888P"
 *
 *  .d8888b.  888  .d8888b.  888  .d8888b.  888
 * d88P  Y88b 888 d88P  Y88b 888 d88P  Y88b 888
 *      .d88P 888      .d88P 888      .d88P 888
 *    .d88P"  888    .d88P"  888    .d88P"  888
 *    888"    888    888"    888    888"    888
 *    888     Y8P    888     Y8P    888     Y8P
 *             "              "              "
 *    888     888    888     888    888     888
 *
 *
 * TODO: add PROPER class and method documentation, just like have
 *       agreed upon hundreds of times!!!!
 */

/**
 * Link table which couples studies with persons and the role they have within the study
 */
class StudyPerson implements java.io.Serializable {
    Person person
    PersonRole role

    static constraints = {
    }
}
