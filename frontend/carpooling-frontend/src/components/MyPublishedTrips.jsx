import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { MapPin, Calendar, Clock, Euro, Users, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useToast } from '@/components/ui/use-toast';

const MyPublishedTrips = () => {
    const { toast } = useToast();
    const [publishedTrips, setPublishedTrips] = useState([]);

    useEffect(() => {
        const savedTrips = localStorage.getItem('myPublishedTrips');
        if (savedTrips) {
            setPublishedTrips(JSON.parse(savedTrips));
        }
    }, []);

    const handleDelete = (tripId) => {
        const updatedTrips = publishedTrips.filter(t => t.id !== tripId);
        setPublishedTrips(updatedTrips);
        localStorage.setItem('myPublishedTrips', JSON.stringify(updatedTrips));

        const allTrips = JSON.parse(localStorage.getItem('publishedTrips') || '[]');
        const updatedAllTrips = allTrips.filter(t => t.id !== tripId);
        localStorage.setItem('publishedTrips', JSON.stringify(updatedAllTrips));

        toast({
            title: "Trajet supprimé",
            description: "Votre trajet a été supprimé avec succès"
        });
    };

    return (
        <div className="space-y-4">
            {publishedTrips.length === 0 ? (
                <div className="glass-effect rounded-2xl p-12 text-center">
                    <p className="text-gray-500 text-lg">Vous n'avez publié aucun trajet</p>
                </div>
            ) : (
                publishedTrips.map((trip, index) => (
                    <motion.div
                        key={trip.id}
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ delay: index * 0.1 }}
                        className="glass-effect rounded-2xl p-6 shadow-lg"
                    >
                        <div className="flex items-start justify-between">
                            <div className="flex-1">
                                <div className="flex items-center gap-3 mb-4">
                                    <div className="flex items-center gap-2">
                                        <MapPin className="w-5 h-5 text-blue-600" />
                                        <span className="font-semibold text-lg">{trip.from}</span>
                                    </div>
                                    <span className="text-gray-400">→</span>
                                    <div className="flex items-center gap-2">
                                        <MapPin className="w-5 h-5 text-purple-600" />
                                        <span className="font-semibold text-lg">{trip.to}</span>
                                    </div>
                                </div>

                                <div className="grid md:grid-cols-4 gap-4 text-gray-700 mb-4">
                                    <div className="flex items-center gap-2">
                                        <Calendar className="w-4 h-4" />
                                        <span>{trip.date}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Clock className="w-4 h-4" />
                                        <span>{trip.time}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Euro className="w-4 h-4" />
                                        <span className="font-semibold">{trip.price}€</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Users className="w-4 h-4" />
                                        <span>{trip.seats} places</span>
                                    </div>
                                </div>

                                {trip.preferences && trip.preferences.length > 0 && (
                                    <div className="flex flex-wrap gap-2">
                                        {trip.preferences.map((pref, idx) => (
                                            <span
                                                key={idx}
                                                className="text-xs bg-blue-100 text-blue-700 px-3 py-1 rounded-full"
                                            >
                        {pref}
                      </span>
                                        ))}
                                    </div>
                                )}
                            </div>

                            <Button
                                variant="destructive"
                                size="icon"
                                onClick={() => handleDelete(trip.id)}
                                className="ml-4"
                            >
                                <Trash2 className="w-4 h-4" />
                            </Button>
                        </div>
                    </motion.div>
                ))
            )}
        </div>
    );
};

export default MyPublishedTrips;