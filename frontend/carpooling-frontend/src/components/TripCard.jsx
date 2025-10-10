import React from 'react';
import { motion } from 'framer-motion';
import { MapPin, Clock, Euro, Users, Star, Car } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useToast } from '@/components/ui/use-toast';

const TripCard = ({ trip }) => {
    const { toast } = useToast();

    const handleBook = () => {
        const bookings = JSON.parse(localStorage.getItem('myBookings') || '[]');
        const newBooking = {
            ...trip,
            bookingId: Date.now().toString(),
            bookedAt: new Date().toISOString(),
            status: 'confirmed'
        };
        bookings.push(newBooking);
        localStorage.setItem('myBookings', JSON.stringify(bookings));

        toast({
            title: "Réservation confirmée ! 🎉",
            description: `Votre place pour ${trip.from} → ${trip.to} est réservée`
        });
    };

    return (
        <motion.div
            whileHover={{ y: -5 }}
            className="glass-effect rounded-2xl p-6 shadow-lg hover:shadow-2xl transition-all"
        >
            <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                        <MapPin className="w-5 h-5 text-blue-600" />
                        <span className="font-semibold text-lg">{trip.from}</span>
                    </div>
                    <div className="flex items-center gap-2 text-gray-600">
                        <MapPin className="w-5 h-5 text-purple-600" />
                        <span className="font-semibold text-lg">{trip.to}</span>
                    </div>
                </div>
                <div className="text-right">
                    <div className="text-3xl font-bold text-blue-600">{trip.price}€</div>
                    <div className="text-sm text-gray-500">par personne</div>
                </div>
            </div>

            <div className="space-y-3 mb-4">
                <div className="flex items-center gap-2 text-gray-700">
                    <Clock className="w-4 h-4" />
                    <span>{trip.date} à {trip.time}</span>
                </div>
                <div className="flex items-center gap-2 text-gray-700">
                    <Users className="w-4 h-4" />
                    <span>{trip.seats} places disponibles</span>
                </div>
                <div className="flex items-center gap-2 text-gray-700">
                    <Car className="w-4 h-4" />
                    <span>{trip.car}</span>
                </div>
            </div>

            <div className="border-t border-gray-200 pt-4 mb-4">
                <div className="flex items-center gap-3 mb-3">
                    <div className="w-12 h-12 rounded-full bg-gradient-to-r from-blue-600 to-purple-600 flex items-center justify-center text-white font-bold text-lg">
                        {trip.driver.name.charAt(0)}
                    </div>
                    <div className="flex-1">
                        <div className="font-semibold">{trip.driver.name}</div>
                        <div className="flex items-center gap-2 text-sm text-gray-600">
                            <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                            <span>{trip.driver.rating}</span>
                            <span>•</span>
                            <span>{trip.driver.trips} trajets</span>
                        </div>
                    </div>
                </div>

                <div className="flex flex-wrap gap-2">
                    {trip.preferences.map((pref, index) => (
                        <span
                            key={index}
                            className="text-xs bg-blue-100 text-blue-700 px-3 py-1 rounded-full"
                        >
              {pref}
            </span>
                    ))}
                </div>
            </div>

            <Button
                onClick={handleBook}
                className="w-full bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700"
            >
                Réserver ce trajet
            </Button>
        </motion.div>
    );
};

export default TripCard;