import React, { useState } from 'react';
import { Helmet } from 'react-helmet';
import { Toaster } from '@/components/ui/toaster';
import Header from '@/components/Header';
import Hero from '@/components/Hero';
import SearchSection from '@/components/SearchSection';
import TripsList from '@/components/TripsList';
import MyTrips from '@/components/MyTrips';
import PublishTrip from '@/components/PublishTrip';

function App() {
    const [activeTab, setActiveTab] = useState('search');

    return (
        <>
            <Helmet>
                <title>Carpooling - Partagez vos trajets quotidiens</title>
                <meta name="description" content="Plateforme de covoiturage pour vos trajets quotidiens. Trouvez ou proposez des trajets facilement et économisez sur vos déplacements." />
            </Helmet>

            <div className="min-h-screen">
                <Header activeTab={activeTab} setActiveTab={setActiveTab} />

                {activeTab === 'search' && (
                    <>
                        <Hero />
                        <SearchSection />
                        <TripsList />
                    </>
                )}

                {activeTab === 'publish' && <PublishTrip />}

                {activeTab === 'mytrips' && <MyTrips />}

                <Toaster />
            </div>
        </>
    );
}

export default App;