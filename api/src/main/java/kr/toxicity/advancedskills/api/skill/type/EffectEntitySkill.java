package kr.toxicity.advancedskills.api.skill.type;

import kr.toxicity.advancedskills.api.skill.Skill;
import kr.toxicity.advancedskills.api.skill.TargetEntitySkill;
import kr.toxicity.advancedskills.api.skill.TargetLocationSkill;

/**
 * Represents entity effect skill
 */
public interface EffectEntitySkill extends Skill, TargetEntitySkill, TargetLocationSkill {
    /**
     * Orient of that skill
     */
    enum Orient {
        CASTER,
        LOCATION
    }
}
