/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.perit.wsstepbystep.db.postgres;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import hu.perit.spvitamin.spring.config.SpringContext;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;

/**
 * #know-how:jpa-auditing
 *
 * @author Peter Nagy
 */

class SpringSecurityAuditorAware implements AuditorAware<String>
{

    @Override
    public Optional<String> getCurrentAuditor()
    {
        return Optional.of(SpringContext.getBean(AuthorizationService.class).getAuthenticatedUser().getUsername());
    }
}