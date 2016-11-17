/*
 * Copyright (c) 2013-2016 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.module.es.search.resolver;

import com.graphaware.common.log.LoggerFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.logging.Log;

public final class ResolverStore {

    private static final Log LOG = LoggerFactory.getLogger(ResolverStore.class);

    private static boolean initialized = false;
    private static KeyToIdResolver resolver;

    public static KeyToIdResolver getResolver(GraphDatabaseService database, String keyProperty) {
        if (!initialized) {
            resolver = initResolver(database, keyProperty);
            initialized = true;
        }

        return resolver;
    }

    private static KeyToIdResolver initResolver(GraphDatabaseService database, String keyProperty) {
        try {
            return new NativeIdResolver(database, keyProperty);
        } catch (ResolverNotApplicable e) {
            // ignore and try next resolver
        }

        try {
            return new UuidResolver(database, keyProperty);
        } catch (ResolverNotApplicable e) {
            // ignore and try next resolver
        }

        throw new RuntimeException("No fitting Key-to-ID resolver found");
    }

}
