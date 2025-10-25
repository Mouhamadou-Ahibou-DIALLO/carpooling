export default function Navbar() {
    return (
        <nav className="flex items-center justify-between px-8 py-4 bg-white shadow">
            <div className="flex items-center space-x-2">
                <span className="text-2xl font-bold text-purple-600">🚗 Carpooling</span>
            </div>
            <div className="flex items-center space-x-6">
                <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Rechercher</button>
                <a href="#" className="text-gray-700 hover:text-blue-600">Publier un trajet</a>
                <a href="#" className="text-gray-700 hover:text-blue-600">Mes trajets</a>
            </div>
            <button className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-100">
                👤 Mon compte
            </button>
        </nav>
    );
}
