package com.openclassrooms.mareu.di;

import com.openclassrooms.mareu.service.DummyMeetingsApiService;
import com.openclassrooms.mareu.service.MeetingsApiService;

/**
 * Injecteur de dépendance pour obtenir une instance de services
 */
public class DI {

    /**
     * Instance variable of MeetingsApiService interface (DummyMeetingsApiService implementation)
     */
    private static MeetingsApiService mMeetingsApiService = new DummyMeetingsApiService();

    /**
     * Get an instance on @{@link MeetingsApiService}
     * @return @{@link MeetingsApiService}
     */
    public static MeetingsApiService getMeetingsApiService() {
        return mMeetingsApiService;
    }

    /**
     * Get always a new instance on @{@link MeetingsApiService}.
     * Useful for tests, so we ensure the context is clean.
     * @return @{@link MeetingsApiService}
     */
    public static MeetingsApiService getNewInstanceMeetingsApiService() {
        return new DummyMeetingsApiService();
    }
}
