package kr.toxicity.advancedskills.api.nms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * NMS version.
 */
@RequiredArgsConstructor
@Getter
public enum NMSVersion {
    V1_19_R3(19,3, 13),
    V1_20_R1(20,1, 15),
    V1_20_R2(20,2, 18),
    V1_20_R3(20,3, 22)
    ;
    private final int version;
    private final int subVersion;
    private final int metaVersion;
}
