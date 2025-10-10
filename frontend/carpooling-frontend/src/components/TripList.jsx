import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import TripCard from '@/components/TripCard';

const TripsList = () => {
    const [trips, setTrips] = useState([]);

    useEffect(() => {
        const savedTrips = localStorage.getItem('publishedTrips');
        if (savedTrips) {
            setTrips(JSON.parse(savedTrips));
        } else {
            const mockTrips = [
                {
                    id: '1',
                    from: 'Paris',
                    to: 'Lyon',
                    date: '2025-10-15',
                    time: '08:00',
                    price: 25,
                    seats: 3,
                    driver: {
                        name: 'Sophie Martin',
                        rating: 4.8,
                        trips: 45
                    },
                    car: 'Renault Clio',
                    preferences: ['Non-fumeur', 'Musique OK']
                },
                {
                    id: '2',
                    from: 'Marseille',
                    to: 'Nice',
                    date: '2025-10-16',
                    time: '14:30',
                    price: 15,
                    seats: 2,
                    driver: {
                        name: 'Thomas Dubois',
                        rating: 4.9,
                        trips: 78
                    },
                    car: 'Peugeot 308',
                    preferences: ['Non-fumeur', 'Animaux OK']
                },
                {
                    id: '3',
                    from: 'Toulouse',
                    to: 'Bordeaux',
                    date: '2025-10-17',
                    time: '10:00',
                    price: 18,
                    seats: 4,
                    driver: {
                        name: 'Marie Leroy',
                        rating: 5.0,
                        trips: 120
                    },
                    car: 'Volkswagen Golf',
                    preferences: ['Non-fumeur', 'Silence apprécié']
                }
            ];
            setTrips(mockTrips);
        }
    }, []);

    return (
        <section className="container mx-auto px-4 py-16">
            <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 0.5 }}
            >
                <h3 className="text-3xl font-bold mb-8 gradient-text">
                    Trajets disponibles
                </h3>

                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {trips.map((trip, index) => (
                        <motion.div
                            key={trip.id}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ delay: 0.1 * index }}
                        >
                            <TripCard trip={trip} />
                        </motion.div>
                    ))}
                </div>

                {trips.length === 0 && (
                    <div className="text-center py-16">
                        <p className="text-gray-500 text-lg">Aucun trajet disponible pour le moment</p>
                    </div>
                )}
            </motion.div>
        </section>
    );
};

export default TripsList;