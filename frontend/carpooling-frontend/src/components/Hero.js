export default function Hero() {
    return (
        <section className="text-center py-16 bg-gradient-to-b from-purple-100 to-white">
            <h1 className="text-4xl font-bold text-purple-700 mb-4">
                Voyagez ensemble, économisez chaque jour
            </h1>
            <p className="text-gray-600 mb-10">
                Partagez vos trajets quotidiens avec des personnes de confiance.<br />
                Économique, écologique et convivial !
            </p>

            <div className="flex justify-center space-x-8 mb-10">
                <div>
                    <p className="text-3xl font-bold text-blue-600">50%</p>
                    <p className="text-gray-500">d’économies en moyenne</p>
                </div>
                <div>
                    <p className="text-3xl font-bold text-purple-600">10k+</p>
                    <p className="text-gray-500">trajets partagés</p>
                </div>
                <div>
                    <p className="text-3xl font-bold text-pink-600">5★</p>
                    <p className="text-gray-500">satisfaction moyenne</p>
                </div>
            </div>

            <div className="bg-white p-6 rounded-xl shadow-md w-11/12 md:w-2/3 mx-auto">
                <form className="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <input type="text" placeholder="Départ" className="border p-3 rounded-lg" />
                    <input type="text" placeholder="Arrivée" className="border p-3 rounded-lg" />
                    <input type="date" className="border p-3 rounded-lg" />
                    <button className="bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-lg px-4 py-3 hover:opacity-90">
                        Rechercher un trajet
                    </button>
                </form>
            </div>
        </section>
    );
}
